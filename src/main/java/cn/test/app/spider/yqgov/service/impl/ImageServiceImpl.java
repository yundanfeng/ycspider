package cn.test.app.spider.yqgov.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

import cn.test.app.spider.yqgov.HttpService;
import cn.test.app.spider.yqgov.controller.req.ClipCallBackReq;
import cn.test.app.spider.yqgov.controller.req.ClipSize;
import cn.test.app.spider.yqgov.dao.IArticleInfoRepo;
import cn.test.app.spider.yqgov.dao.IItemInfoRepo;
import cn.test.app.spider.yqgov.entity.ArticleInfo;
import cn.test.app.spider.yqgov.global.Globals;
import cn.test.app.spider.yqgov.global.YQGovSpiderConfig;
import cn.test.app.spider.yqgov.service.IIMageService;
import cn.test.app.spider.yqgov.service.bean.ClipPicture;
import cn.test.app.spider.yqgov.service.bean.ClipReq;
import cn.test.app.spider.yqgov.service.bean.HttpResult;
import cn.test.app.spider.yqgov.service.bean.PictureSize;
import sun.misc.BASE64Decoder;

/**
 * @remark
 * @author luzh
 * @createTime 2017年8月17日 下午5:11:52
 */
@SuppressWarnings("restriction")
@Service
public class ImageServiceImpl implements IIMageService {

	private static String[] sizes = { "320x400", "240x300", "160x200", "500x280", "375x210", "246x138" };
	private static Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);

	private Gson gson = new Gson();

	@Resource
	private HttpService httpService;

	@Resource
	private YQGovSpiderConfig config;

	@Resource
	private IItemInfoRepo itemRepo;

	@Resource
	private IArticleInfoRepo articleRepo;
	
	@Override
	public void clip(String url) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		List<ArticleInfo> articles = articleRepo.findByUrl(url);
		for (ArticleInfo info : articles) {
			if (null == info.getImages()) {
				return;
			}
			ClipReq req = new ClipReq();
			req.setAccesskey(config.getClipPicAccesskey());
			req.setUsrName(config.getClipPicUserName());
			List<ClipPicture> pictures = new ArrayList<>();
			int count = 0;
			for (String imageUrl : info.getImages().split(Globals.IMAGE_SPLIT)) {
				List<PictureSize> clibSizes = new ArrayList<>();
				count++;
				for (String size : sizes) {
					// 裁剪接口很辣鸡，新闻系统也很辣鸡，图片必须设置成jpg格式
					clibSizes.add(new PictureSize(size, "/zaker/" + sdf.format(new Date()) + "/" + getSaveName(info, size, count) + ".jpg"));
				}
				ClipPicture picture = new ClipPicture(imageUrl, getPicId(info, count), false, true, "",
						config.getUrlPath() + "/image/clipCallBack", "", clibSizes);
				pictures.add(picture);
			}
			req.setPictures(pictures);
			logger.info("by luzh-> clip req: {}", gson.toJson(req));
			HttpResult result = new HttpResult();
			result.setBody("");
			try {
				while (!result.getBody().contains("\"retCode\":0")) {
					result = httpService.doPostJson(config.getClipPicAddress(), gson.toJson(req));
					logger.info("by luzh-> clip article {} first result {}", info.getId(), result.toString());
				}
			} catch (Exception e) {
				logger.error("by luzh-> clip error {}.", e.getMessage(), e);
			}
		}
	}
	
	public static String getPicId(ArticleInfo info, int index) {
		return Globals.IMPORT_KEY + info.getDate() + "-" + info.getId() + "-" + index;
	}
	
	public static String getSaveName(ArticleInfo info, String size, int index) {
		return Globals.IMPORT_KEY + info.getDate() + "-" + info.getId() + "_" + size + "_" + index;
	}

	@Override
	public void downloadImage(String url) throws Exception {
		String imageFolderPath = config.getImageFolderPath();
		String imageUrl = config.getImageUrl();
		if (Globals.isNull(imageFolderPath) || Globals.isNull(imageUrl)) {
			logger.info("by luzh-> downloadImage: image config is error, quit download.");
			return;
		}
		File imagePathFile = new File(imageFolderPath);
		if (!imagePathFile.exists()) {
			logger.info("by luzh-> downloadImage: image folder {} is not exists quit download.", imageFolderPath);
			return;
		}
		ArticleInfo article = articleRepo.findByUrl(url).get(0);
		StringBuffer newImages = new StringBuffer();
		String content = article.getContent();
		int count = 0;
		for (String address : article.getImages().split(Globals.IMAGE_SPLIT)) {
			count++;
			byte[] imageByte = null;
			try {
				imageByte = downloadImage(httpService, address);
			} catch (Exception e) {
				// TODO: handle exception
			}
			if (null == imageByte) {
				content.replaceAll(address, "");
				logger.info("by luzh-> downloadImage: download address {} faild.");
				continue;
			}
			FileOutputStream output = new FileOutputStream(
					imagePathFile.getAbsolutePath() + "/" + getImageName(article, count, address));
			output.write(imageByte);
			output.close();
			String newPath = imageUrl + "/" + getImageName(article, count, address);
			newImages.append(Globals.IMAGE_SPLIT).append(newPath);
			content = content.replaceAll(address, newPath);
			logger.info("by luzh-> downloadImage: article 【{}】replace image address {} to new address {}.",
					article.getTitle(), address, newPath);
		}
		article.setContent(content);
		article.setImages(newImages.toString().substring(Globals.IMAGE_SPLIT.length()));
		article.setImageCount(count);
		articleRepo.save(article);
		logger.info("by luzh-> downloadImage: article 【{}】downloadImage finished", article.getTitle());
		clip(article.getUrl());
		return;
	}

	public static byte[] downloadImage(HttpService httpService, String address) throws Exception {
		if (address.startsWith("http")) {
			return httpService.downloadImage(address);
		}
		if (address.startsWith("data:image")) {
			return convertBase64DataToImage(address.split(";base64,")[1]);
		}
		return null;
	}

    public static byte[] convertBase64DataToImage(String base64ImgData) throws IOException {
		BASE64Decoder d = new BASE64Decoder();
		return d.decodeBuffer(base64ImgData);
	}

	public static String getImageName(ArticleInfo info, int index, String address) {
		String type = getImageType(address);
		return Globals.IMPORT_KEY + info.getDate() + "-" + info.getId() + "-" + index + type;
	}

	public static String getImageType(String address) {
		if (address.startsWith("http")) {
			return address.substring(address.lastIndexOf("."));
		}
		if (address.startsWith("data:image")) {
			return "." + address.split(";base64,")[0].split("data:image/")[1];
		}
		return "";
	}

	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
	public void callBack(ClipCallBackReq req) {
		cn.test.app.spider.yqgov.controller.req.ClipPicture picture = req.getPicture();
		String picId = picture.getPicId();
		Integer articleId = Integer.parseInt(picId.split("-")[3]);
		int count = Integer.parseInt(picId.split("-")[4]);
		// 如果不是0，那就将对应的content里面的img标签中的地址设置为空
		if (req.getPicture().getResultCode() != 0) {
			ArticleInfo articleInfo = articleRepo.findById(articleId);
			String oldImage = articleInfo.getImages().split(Globals.IMAGE_SPLIT)[count-1];
			articleInfo.setContent(articleInfo.getContent().replaceAll(oldImage, ""));
			logger.info("by luzh-> callBack: article 【{}】image {} clip error.", articleInfo.getTitle(), oldImage);
			articleInfo.setImageCount(articleInfo.getImageCount().intValue() - 1);
			articleRepo.save(articleInfo);
			if (0 == articleInfo.getImageCount().intValue()) {
				itemRepo.updateFinishByUrl(true, articleInfo.getUrl());
				logger.info("by luzh-> callBack: article 【{}】image clip finished. all info done!!!", articleInfo.getTitle());
			}
			return;
		}
		List<ClipSize> sizes = picture.getClibSizes();
		StringBuffer buffer = new StringBuffer();
		for (ClipSize size : sizes) {
			buffer.append(Globals.IMAGE_SPLIT).append(size.getClipPicUrl());
		}
		if (buffer.toString().length() > Globals.IMAGE_SPLIT.length()) {
			String newImages = buffer.toString().substring(Globals.IMAGE_SPLIT.length());
			ArticleInfo articleInfo = articleRepo.findById(articleId);
			String oldImage = articleInfo.getImages().split(Globals.IMAGE_SPLIT)[count-1];
			articleInfo.setClipImages(articleInfo.getClipImages() == null ? "" + newImages : articleInfo.getClipImages() + Globals.IMAGE_SPLIT + newImages);
			articleInfo.setContent(articleInfo.getContent().replaceAll(oldImage, newImages.split(Globals.IMAGE_SPLIT)[0]));
			articleInfo.setImageCount(articleInfo.getImageCount().intValue() - 1);
			articleRepo.save(articleInfo);
			// 如果有一张图片已经裁剪成功，就标记该新闻整体完成
//			if (articleInfo.getImages().split(Globals.IMAGE_SPLIT).length > articleInfo.getImageCount().intValue()) {
//				itemRepo.updateFinishByUrl(true, articleInfo.getUrl());
//				logger.info("by luzh-> callBack: article 【{}】image clip finished. all info done!!!", articleInfo.getTitle());
//			}
			if (0 == articleInfo.getImageCount().intValue()) {
				itemRepo.updateFinishByUrl(true, articleInfo.getUrl());
				logger.info("by luzh-> callBack: article 【{}】image clip finished. all info done!!!", articleInfo.getTitle());
			}
		}
	}
}
