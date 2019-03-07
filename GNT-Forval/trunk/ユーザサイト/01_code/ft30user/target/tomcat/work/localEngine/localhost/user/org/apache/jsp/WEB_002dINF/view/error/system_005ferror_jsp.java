package org.apache.jsp.WEB_002dINF.view.error;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.seasar.struts.util.MessageResourcesUtil;
import jp.co.forvaltel.common.util.FtResourcesUtil;
import jp.co.forvaltel.common.constant.CommonConst;

public final class system_005ferror_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList(2);
    _jspx_dependants.add("/WEB-INF/view/common/common.jsp");
    _jspx_dependants.add("/WEB-INF/tld/ft.tld");
  }

  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005ftiles_005finsert_0026_005ftemplate_005fflush;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005ftiles_005fput_0026_005fname;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fft_005flabel_0026_005fkey_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005ftiles_005fput_0026_005ftype_005fname;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.AnnotationProcessor _jsp_annotationprocessor;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _005fjspx_005ftagPool_005ftiles_005finsert_0026_005ftemplate_005fflush = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005ftiles_005fput_0026_005fname = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fft_005flabel_0026_005fkey_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005ftiles_005fput_0026_005ftype_005fname = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
  }

  public void _jspDestroy() {
    _005fjspx_005ftagPool_005ftiles_005finsert_0026_005ftemplate_005fflush.release();
    _005fjspx_005ftagPool_005ftiles_005fput_0026_005fname.release();
    _005fjspx_005ftagPool_005fft_005flabel_0026_005fkey_005fnobody.release();
    _005fjspx_005ftagPool_005ftiles_005fput_0026_005ftype_005fname.release();
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html;charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("  \r\n");
      out.write("  \r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      //  tiles:insert
      org.apache.struts.taglib.tiles.InsertTag _jspx_th_tiles_005finsert_005f0 = (org.apache.struts.taglib.tiles.InsertTag) _005fjspx_005ftagPool_005ftiles_005finsert_0026_005ftemplate_005fflush.get(org.apache.struts.taglib.tiles.InsertTag.class);
      _jspx_th_tiles_005finsert_005f0.setPageContext(_jspx_page_context);
      _jspx_th_tiles_005finsert_005f0.setParent(null);
      // /WEB-INF/view/error/system_error.jsp(5,0) name = template type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_tiles_005finsert_005f0.setTemplate("/WEB-INF/view/common/layout.jsp");
      // /WEB-INF/view/error/system_error.jsp(5,0) name = flush type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_tiles_005finsert_005f0.setFlush(true);
      int _jspx_eval_tiles_005finsert_005f0 = _jspx_th_tiles_005finsert_005f0.doStartTag();
      if (_jspx_eval_tiles_005finsert_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write("\r\n");
          out.write("\t<!-- タイトル設定 -->\r\n");
          out.write("\t");
          if (_jspx_meth_tiles_005fput_005f0(_jspx_th_tiles_005finsert_005f0, _jspx_page_context))
            return;
          out.write("\t\r\n");
          out.write("\t\r\n");
          out.write("\t<!-- 表示部分実装 -->\t\r\n");
          out.write("\t");
          //  tiles:put
          org.apache.struts.taglib.tiles.PutTag _jspx_th_tiles_005fput_005f1 = (org.apache.struts.taglib.tiles.PutTag) _005fjspx_005ftagPool_005ftiles_005fput_0026_005ftype_005fname.get(org.apache.struts.taglib.tiles.PutTag.class);
          _jspx_th_tiles_005fput_005f1.setPageContext(_jspx_page_context);
          _jspx_th_tiles_005fput_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_tiles_005finsert_005f0);
          // /WEB-INF/view/error/system_error.jsp(10,1) name = name type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_tiles_005fput_005f1.setName("content");
          // /WEB-INF/view/error/system_error.jsp(10,1) name = type type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_tiles_005fput_005f1.setType("string");
          int _jspx_eval_tiles_005fput_005f1 = _jspx_th_tiles_005fput_005f1.doStartTag();
          if (_jspx_eval_tiles_005fput_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_tiles_005fput_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_tiles_005fput_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_tiles_005fput_005f1.doInitBody();
            }
            do {
              out.write("\r\n");
              out.write("    <div id=\"ft-container\">\r\n");
              out.write("\t    <div class=\"ft-box-error\">\r\n");
              out.write("\t        <div class=\"ft-box-error-inner inner-error\" data-role=\"content\">\r\n");
              out.write("\t            <dl class=\"ui-listview ui-listview-inset ui-corner-all ui-shadow\">\r\n");
              out.write("\t                <dt class=\"ui-li ui-li-divider ui-bar-b ui-corner-top\">\r\n");
              out.write("\t\t\t\t\t\t");

						out.println(MessageResourcesUtil.getMessage(CommonConst.MSG_ID_ERRORS_SYSTEM_TITLE));
						
              out.write("\r\n");
              out.write("\t               \t</dt>\r\n");
              out.write("\t                <dd class=\"ui-li ui-li-static ui-body-c ui-corner-bottom\">\r\n");
              out.write("\t\t\t\t\t\t");

//	 					TTM_DEV BEGIN EDIT 20180312
//							out.println(MessageResourcesUtil.getMessage(CommonConst.MSG_ID_ERRORS_SYSTEM, FtResourcesUtil.getItemValue(CommonConst.ITEM_KEY_SYETEM_ERROR_TEL), FtResourcesUtil.getItemValue(CommonConst.ITEM_KEY_SYETEM_ERROR_MAIL)));
							out.println(MessageResourcesUtil.getMessage(CommonConst.MSG_ID_ERRORS_SYSTEM));
//		 				TTM_DEV END EDIT 20180312
						
              out.write("\r\n");
              out.write("\t                </dd>\r\n");
              out.write("\t            </dl>\r\n");
              out.write("\t        </div>\r\n");
              out.write("\t        \r\n");
              out.write("\t    </div>\r\n");
              out.write("    </div>\r\n");
              out.write("\t");
              int evalDoAfterBody = _jspx_th_tiles_005fput_005f1.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_tiles_005fput_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.popBody();
            }
          }
          if (_jspx_th_tiles_005fput_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005ftiles_005fput_0026_005ftype_005fname.reuse(_jspx_th_tiles_005fput_005f1);
            return;
          }
          _005fjspx_005ftagPool_005ftiles_005fput_0026_005ftype_005fname.reuse(_jspx_th_tiles_005fput_005f1);
          out.write("\r\n");
          out.write("\t\r\n");
          out.write("\t<!-- javascript実装 -->\t\r\n");
          out.write("\t");
          if (_jspx_meth_tiles_005fput_005f2(_jspx_th_tiles_005finsert_005f0, _jspx_page_context))
            return;
          out.write("\r\n");
          out.write("\t\r\n");
          int evalDoAfterBody = _jspx_th_tiles_005finsert_005f0.doAfterBody();
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
      }
      if (_jspx_th_tiles_005finsert_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005ftiles_005finsert_0026_005ftemplate_005fflush.reuse(_jspx_th_tiles_005finsert_005f0);
        return;
      }
      _005fjspx_005ftagPool_005ftiles_005finsert_0026_005ftemplate_005fflush.reuse(_jspx_th_tiles_005finsert_005f0);
      out.write('\r');
      out.write('\n');
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try { out.clearBuffer(); } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }

  private boolean _jspx_meth_tiles_005fput_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_tiles_005finsert_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  tiles:put
    org.apache.struts.taglib.tiles.PutTag _jspx_th_tiles_005fput_005f0 = (org.apache.struts.taglib.tiles.PutTag) _005fjspx_005ftagPool_005ftiles_005fput_0026_005fname.get(org.apache.struts.taglib.tiles.PutTag.class);
    _jspx_th_tiles_005fput_005f0.setPageContext(_jspx_page_context);
    _jspx_th_tiles_005fput_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_tiles_005finsert_005f0);
    // /WEB-INF/view/error/system_error.jsp(7,1) name = name type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_tiles_005fput_005f0.setName("title");
    int _jspx_eval_tiles_005fput_005f0 = _jspx_th_tiles_005fput_005f0.doStartTag();
    if (_jspx_eval_tiles_005fput_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_tiles_005fput_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_tiles_005fput_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_tiles_005fput_005f0.doInitBody();
      }
      do {
        if (_jspx_meth_ft_005flabel_005f0(_jspx_th_tiles_005fput_005f0, _jspx_page_context))
          return true;
        int evalDoAfterBody = _jspx_th_tiles_005fput_005f0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_tiles_005fput_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_tiles_005fput_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ftiles_005fput_0026_005fname.reuse(_jspx_th_tiles_005fput_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005ftiles_005fput_0026_005fname.reuse(_jspx_th_tiles_005fput_005f0);
    return false;
  }

  private boolean _jspx_meth_ft_005flabel_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_tiles_005fput_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ft:label
    jp.co.forvaltel.common.taglib.LabelTag _jspx_th_ft_005flabel_005f0 = (jp.co.forvaltel.common.taglib.LabelTag) _005fjspx_005ftagPool_005fft_005flabel_0026_005fkey_005fnobody.get(jp.co.forvaltel.common.taglib.LabelTag.class);
    _jspx_th_ft_005flabel_005f0.setPageContext(_jspx_page_context);
    _jspx_th_ft_005flabel_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_tiles_005fput_005f0);
    // /WEB-INF/view/error/system_error.jsp(7,25) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_ft_005flabel_005f0.setKey("system.title.9000");
    int _jspx_eval_ft_005flabel_005f0 = _jspx_th_ft_005flabel_005f0.doStartTag();
    if (_jspx_th_ft_005flabel_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fft_005flabel_0026_005fkey_005fnobody.reuse(_jspx_th_ft_005flabel_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fft_005flabel_0026_005fkey_005fnobody.reuse(_jspx_th_ft_005flabel_005f0);
    return false;
  }

  private boolean _jspx_meth_tiles_005fput_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_tiles_005finsert_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  tiles:put
    org.apache.struts.taglib.tiles.PutTag _jspx_th_tiles_005fput_005f2 = (org.apache.struts.taglib.tiles.PutTag) _005fjspx_005ftagPool_005ftiles_005fput_0026_005ftype_005fname.get(org.apache.struts.taglib.tiles.PutTag.class);
    _jspx_th_tiles_005fput_005f2.setPageContext(_jspx_page_context);
    _jspx_th_tiles_005fput_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_tiles_005finsert_005f0);
    // /WEB-INF/view/error/system_error.jsp(36,1) name = name type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_tiles_005fput_005f2.setName("myscript");
    // /WEB-INF/view/error/system_error.jsp(36,1) name = type type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_tiles_005fput_005f2.setType("string");
    int _jspx_eval_tiles_005fput_005f2 = _jspx_th_tiles_005fput_005f2.doStartTag();
    if (_jspx_eval_tiles_005fput_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_tiles_005fput_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_tiles_005fput_005f2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_tiles_005fput_005f2.doInitBody();
      }
      do {
        out.write("\r\n");
        out.write("\t \r\n");
        out.write("\t");
        int evalDoAfterBody = _jspx_th_tiles_005fput_005f2.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_tiles_005fput_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_tiles_005fput_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ftiles_005fput_0026_005ftype_005fname.reuse(_jspx_th_tiles_005fput_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005ftiles_005fput_0026_005ftype_005fname.reuse(_jspx_th_tiles_005fput_005f2);
    return false;
  }
}
