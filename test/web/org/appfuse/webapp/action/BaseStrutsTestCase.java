package org.appfuse.webapp.action;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.cactus.WebRequest;
import org.apache.cactus.client.authentication.FormAuthentication;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.beanutils.BeanUtils;
import org.appfuse.Constants;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.appfuse.webapp.form.UserForm;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import servletunit.struts.CactusStrutsTestCase;


/**
 * This class is extended by all ActionTests.  It basically
 * contains common methods that they might use.
 *
 * <p>
 * <a href="BaseStrutsTestCase.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @version $Revision: 1.2 $ $Date: 2004/03/18 20:33:03 $
 */
public class BaseStrutsTestCase extends CactusStrutsTestCase {
    //~ Instance fields ========================================================

    private static Log log = LogFactory.getLog(BaseStrutsTestCase.class);
    protected Object conn = null;
    protected User user = null;
    protected ResourceBundle rb = null;
    protected ResourceBundle login = null;
    protected WebApplicationContext ctx = null;
    
    //~ Constructors ===========================================================

    public BaseStrutsTestCase(String name) {
        super(name);
        // Since a ResourceBundle is not required for each class, just
        // do a simple check to see if one exists
        String className = this.getClass().getName();

        try {
            rb = ResourceBundle.getBundle(className);
        } catch (MissingResourceException mre) {
            //log.warn("No resource bundle found for: " + className);
        }
        login = ResourceBundle.getBundle(LoginServletTest.class.getName());
    }

    //~ Methods ================================================================

    public void setUp() throws Exception {
        super.setUp();
        // populate the userForm and place into session
        String username = login.getString("username");
        ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(
        		this.getSession().getServletContext());
        UserManager userMgr = (UserManager) ctx.getBean("userManager");
        user = (User) userMgr.getUser(username);
        getSession().setAttribute(Constants.USER_KEY, user);
    }
    
    protected void tearDown() throws Exception {
        ctx = null;
    }
    
    /**
     * This method will be called before executing any tests
     * @param request The current client's request
     */
    public void begin(WebRequest request) {
        request.setRedirectorName("ServletRedirectorSecure");
        request.setAuthentication(new FormAuthentication(
                login.getString("admin.username"),
                login.getString("encryptedPassword")));
    }
}
