package jp.co.forvaltel.user.interceptor;

import org.aopalliance.intercept.MethodInvocation;

import jp.co.forvaltel.common.exception.FtAuthenticationException;
import jp.co.forvaltel.common.interceptor.AbstractActionInterceptor;
/**
 * @author TTM
 *
 */
public class UserActionInterceptor extends AbstractActionInterceptor {

	/**
	 * デフォルトシリアルバージョンID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Action共通前処理
	 */
	@Override
	protected Object beforeExecute(MethodInvocation invocation) throws FtAuthenticationException {
		return null;
	}
	
//	@Override
//	protected Object exForActionHandle(Throwable exception, MethodInvocation invocation) throws Throwable {
//		Object ret = null;
//	    
//	    boolean isExcute = isExecuteMethod(invocation).booleanValue();
//	    if (!isExcute) {
//	      throw exception;
//	    }
//	    ret = ((Execute)invocation.getMethod().getAnnotation(Execute.class)).input();
//	    if ((exception instanceof OptimisticLockException))
//	    {
//	      addErrors("errors.optimisticlock", new String[0]);
//	    }
//	    else if ((exception instanceof FtApplicationException))
//	    {
//	      setRollbackOnly();
//	      
//	      FtApplicationException ftApEx = (FtApplicationException)exception;
//	      if ("errors.deivetAutority".equals(ftApEx.msgKey))
//	      {
//	        outputLog(invocation.getClass(), ftApEx.getMessage(), exception);
//	        ret = "/error/http403.jsp";
//	      }
//	      else
//	      {
//	        addErrors(ftApEx.msgKey, ftApEx.msgParam);
//	      }
//	    }
//	    else if ((exception instanceof FtSystemException))
//	    {
//	      setRollbackOnly();
//	      if (isInitDispMethod(invocation).booleanValue())
//	      {
//	        ret = "/error/system_error.jsp";
//	      }
//	      else
//	      {
//	        String tel = FtResourcesUtil.getItemValue("system.error.tel");
//	        String mail = FtResourcesUtil.getItemValue("system.error.mail");
//	        addErrors("errors.system", new String[] { tel, mail });
//	      }
//	      FtSystemException ftSysEx = (FtSystemException)exception;
//	      outputLog(invocation.getClass(), ftSysEx.getMessage(), exception);
//	    }
//	    else
//	    {
//	      if (isInitDispMethod(invocation).booleanValue())
//	      {
//	        ret = "/error/system_error.jsp";
//	      }
//	      else
//	      {
//	        String tel = FtResourcesUtil.getItemValue("system.error.tel");
//	        String mail = FtResourcesUtil.getItemValue("system.error.mail");
//	        addErrors("errors.system", new String[] { tel, mail });
//	      }
//	      outputLog(invocation.getClass(), exception.getMessage(), exception);
//	    }
//	    return ret;
//	}
//
//	private void addErrors(String key, String... param) {
//		ActionMessages errors = new ActionMessages();
//		errors.add("org.apache.struts.action.GLOBAL_MESSAGE", new ActionMessage(key, param));
//		ActionMessagesUtil.addErrors(RequestUtil.getRequest(), errors);
//	}
	
	@Override
	protected boolean afterExecute(MethodInvocation invocation,
			Object returnBoject) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void outputLog(Class<?> targetClass, String message, Throwable ex) {
		// TODO Auto-generated method stub
		
	}

}
