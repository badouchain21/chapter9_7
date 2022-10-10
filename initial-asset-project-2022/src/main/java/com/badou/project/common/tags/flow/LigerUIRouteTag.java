package com.badou.project.common.tags.flow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.log4j.Logger;

import com.badou.bpms.beans.instance.NodeInstanceBean;
import com.badou.bpms.beans.instance.RouteInstanceBean;
import com.badou.bpms.engine.flow.service.IFlowService;
import com.badou.bpms.engine.flow.valueobject.FlwFlow;
import com.badou.bpms.engine.flow.valueobject.FlwNode;
import com.badou.bpms.engine.flow.valueobject.FlwRoute;
import com.badou.bpms.iface.IBpmsFlowInstanceService;
import com.badou.brms.util.InstanceFactory;
import com.badou.tools.common.util.StringUtils;

/**
 * @author chenjiabao
 * @date 2019年7月2日下午2:18:47
 * @todo ligerUI路由标签类
 */
public class LigerUIRouteTag extends BodyTagSupport{

	protected static Logger logger = Logger.getLogger(LigerUIRouteTag.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -4414945334179837557L;

	private String worklistId;
	/**
	 * 流程ID
	 */
	private String fid;
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {
		JspWriter out = pageContext.getOut();
		if(StringUtils.isNotBlank(worklistId)){
			IBpmsFlowInstanceService bpmsFlowInstanceService = InstanceFactory.getInstance(IBpmsFlowInstanceService.class);
			List<RouteInstanceBean> routes = bpmsFlowInstanceService.getWorklistRoutes(worklistId);
			NodeInstanceBean nodeInstance = bpmsFlowInstanceService.getNodeInstance(worklistId);
			
			try {
				out.print("<input type='hidden' id='flgFullScreen' name='flgFullScreen' value='"+nodeInstance.isFlgFullScreen()+"'/>");
				out.print("<input type='hidden' id='worklistId' name='worklistId' value='"+worklistId+"'/>");
				out.print("<input type='hidden' id='handleWay' name='handleWay' value='"+nodeInstance.getHandleWay()+"'/>");
				out.print("<input type='hidden' id='nodeInstanceId' name='nodeInstanceId' value='"+nodeInstance.getId()+"'/>");
				out.print(makeRouteButtons(routes));
				out.flush();
			} catch (IOException e) {
				logger.error(e);
			}
		}else if(StringUtils.isNotBlank(fid)){//拟稿时的路由
			IFlowService flowService = InstanceFactory.getInstance(IFlowService.class);
			FlwFlow flow = flowService.findById(fid);
			FlwNode firstNode = flow.getFirstNode();
			List<RouteInstanceBean> routes = new ArrayList<RouteInstanceBean>();
			for(FlwRoute r : firstNode.getRouteSet()){
				routes.add(new RouteInstanceBean(r.getId(), r.getId(), r.getName(), r.getMemo(), r.getPriority(), r.getFlgAttribute(), r.getIsSave(), r.getFlowSubmitFormNoValid()));
			}
			try {
				out.print("<input type='hidden' id='flgFullScreen' name='flgFullScreen' value='"+firstNode.getFlgFullScreen()+"'/>");
				out.print("<input type='hidden' id='worklistId' name='worklistId' value=''/>");
				out.print("<input type='hidden' id='flowId' name='flowId' value='"+flow.getId()+"'/>");
				out.print("<input type='hidden' id='busiType' name='busiType' value='"+flow.getCommBusi().getId()+"'/>");
				out.print("<input type='hidden' id='handleWay' name='handleWay' value='"+firstNode.getHandleTypeway().getId()+"'/>");
				out.print(makeRouteButtons(routes));
				out.flush();
			} catch (IOException e) {
				logger.error(e);
			}
		}
		return EVAL_PAGE;
	}
	
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午6:07:47
	 * @todo 获取路由按钮集合
	 * @param routes
	 * @return js字符串
	 */
	private String makeRouteButtons(List<RouteInstanceBean> routes){
		/*StringBuilder sbuilder = new StringBuilder();
		sbuilder.append("<div class=\"form-top-bar\"><div class=\"form-bar-inner\">");
		for(RouteInstanceBean r : routes){
			int width = r.getRouteName().length()*15;
			sbuilder.append("<div id=\""+r.getId()+"\" attr=\""+(r.getFlgAttribute() != null ? r.getFlgAttribute() : "")+"\" class=\"l-dialog-btn l-route-btn l-routeSubmit\" style=\"width:"+width+"px\" routeId=\""+r.getId()+"\"><div class=\"l-dialog-btn-l\"></div>")
			.append("<div class=\"l-dialog-btn-r\"></div><div class=\"l-dialog-btn-inner\">"+r.getRouteName()+"</div></div>");
		}
		sbuilder.append("</div></div>");*/
		
		StringBuilder sbuilder = new StringBuilder();
		sbuilder.append("<script>var menuInner = $('#menuInner');");
		//遍历环节下路由集，添加到页面id为menuInner的工具栏中
		for(RouteInstanceBean r : routes){
			sbuilder.append("menuInner.prepend('<button id=\""+r.getId()+"\" attr=\""+(r.getFlgAttribute() != null ? r.getFlgAttribute() : "")+"\" class=\"btn btn-normal btn-blue l-routeSubmit\" flowSubmitFormNoValid=\""+r.getFlowSubmitFormNoValid()+"\" isSave=\""+r.getIsSave()+"\" routeid=\""+r.getId()+"\">"+r.getRouteName()+"</button>');");
		}
		sbuilder.append("</script>");
		
		return sbuilder.toString();
	}


	public String getWorklistId() {
		return worklistId;
	}

	public void setWorklistId(String worklistId) {
		this.worklistId = worklistId;
	}

	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}
}
