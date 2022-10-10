package com.badou.project.moduledemo.web.fun3;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.badou.brms.base.support.struts.struts2.JsonEditTemplateAction;
import com.badou.project.moduledemo.help.AttachImgCaches;
import com.badou.project.moduledemo.help.AttachImgCaches.AttachImg;
import com.badou.project.moduledemo.help.CommonAttachByAttachId;
import com.badou.project.moduledemo.model.Fun3DemoEntity;
import com.badou.project.moduledemo.service.IFun3DemoService;
import com.badou.project.moduledemo.web.form.Fun3DemoForm;
import com.badou.tools.common.util.ParamUtils;
/**
 * 功能1示例编辑
 * <p>对应的请求路径是：${context}/moduledemo/Fun2/Fun2demoedit/方法名.do
 * <p>对应的请求默认页面是：${context}/WEB-INF/jsp/moduledemo/Fun2/Fun2demoedit_form.jsp
 * @author xiangsf 2013-1-18
 *
 */
@RestController
public class Fun3DemoEditAction extends JsonEditTemplateAction<Fun3DemoEntity, Serializable, Fun3DemoForm> {

	@Autowired
	private IFun3DemoService fun3DemoService;

	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午5:53:36
	 * @todo 注入service
	 * @param fun3DemoService
	 */
	@Autowired
	public void setFun3DemoService(IFun3DemoService fun3DemoService) {
		this.fun3DemoService = fun3DemoService;
		super.setBaseService(fun3DemoService);
	}
	
	/**
	 * 在页面上显示图片
	 * @throws Exception if has error(包装一层错误信息给前台对用户进行提示)
	 * */
	public void downloadGoodsImgCache() throws Exception {
		OutputStream outStream = null;
		try {
			String tempId = request.getParameter("tempId");
			CommonAttachByAttachId commonAttachByAttachId = new CommonAttachByAttachId();
			String imgid = commonAttachByAttachId.findTempIdByattachId(tempId);
			AttachImgCaches.AttachImg goodsImg = AttachImgCaches.getImg(imgid);
			outStream = response.getOutputStream();
			if (goodsImg != null) {
				response.setContentType("application/octet-stream;");
				response.setCharacterEncoding("UTF-8");
				response.setHeader("Content-Disposition","attachment; filename="
								+ (new String(goodsImg.getName().getBytes("GBK"), "iso8859-1") + goodsImg.getSuffix()));
				IOUtils.copy(goodsImg.getIs(), outStream);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		} finally {
			if (outStream != null) {
				outStream.close();
			}
		}
	}

	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午5:55:45
	 * @todo 下载商品图片
	 * @throws Exception if has error(包装一层错误信息给前台对用户进行提示)
	 */
	public void downloadGoodsImgCache1() throws Exception {
		try {
			String tempId = request.getParameter("tempId");
			CommonAttachByAttachId commonAttachByAttachId = new CommonAttachByAttachId();
			String imgid = commonAttachByAttachId.findTempIdByattachId(tempId);
			AttachImgCaches.AttachImg goodsImg = AttachImgCaches.getImg(imgid);
			OutputStream outStream = response.getOutputStream();
			if (goodsImg != null) {
				response.setContentType("application/octet-stream;");
				response.setCharacterEncoding("UTF-8");
				response.setHeader("Content-Disposition","attachment; filename="
								+ (new String(goodsImg.getName().getBytes("GBK"), "iso8859-1") + goodsImg.getSuffix()));
				IOUtils.copy(goodsImg.getIs(), outStream);
			}
			outStream.close();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}

	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午5:56:01
	 * @todo 下载通知图片
	 * @throws Exception if has error(包装一层错误信息给前台对用户进行提示)
	 */
	public void downloadTopicImgCache() throws Exception {
		OutputStream outStream = null;
		try {
			String tempId = request.getParameter("tempId");
			AttachImg goodsImg = AttachImgCaches.getImg(tempId);
			response.setContentType("application/octet-stream;");
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ (new String(goodsImg.getName().getBytes("GBK"),"iso8859-1") + goodsImg.getSuffix()));
			outStream = response.getOutputStream();
			IOUtils.copy(goodsImg.getIs(), outStream);
		} finally {
			if (outStream != null) {
				outStream.close();
			}
		}
	}

	private File attach;
	private String attachFileName;

	public File getAttach() {
		return attach;
	}

	public void setAttach(File attach) {
		this.attach = attach;
	}

	public String getAttachFileName() {
		return attachFileName;
	}

	public void setAttachFileName(String attachFileName) {
		this.attachFileName = attachFileName;
	}

	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午5:56:27
	 * @todo 临时保存上传的文件
	 * @throws Exception if has error(包装一层错误信息给前台对用户进行提示)
	 */
	public void uploadGoodsImg() throws Exception {
		try {
			String tempId = ParamUtils.getParameter(request, "tempId", "");
			if (attach != null) {
				InputStream is = new FileInputStream(attach);
				attachFileName = attachFileName.substring(0,attachFileName.lastIndexOf("."));
				tempId = AttachImgCaches.storeImg(tempId,new AttachImgCaches.AttachImg(attachFileName, null, 0,is));
			}
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();
			out = response.getWriter();
			out.print(tempId);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	
	/*@Action(interceptorRefs = @InterceptorRef("baseJsonStack"))
	@DispatcherResult
	@Override
	public String edit() throws Exception {
		String id=request.getParameter("id");
		String pid=request.getParameter("pid");
		request.setAttribute("pid", pid);
		if(StringUtils.isNotBlank(id))
		{
			Fun3DemoEntity fun3Demo= Fun3DemoService.get(id);
			if(fun3Demo!=null)
			{
				request.setAttribute("smallIconid", fun3Demo.getSmallIcon());
				request.setAttribute("bigIconid", fun3Demo.getBigIcon());
			}
		}
		return "edit";
	}*/
	
}
