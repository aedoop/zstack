package org.zstack.header.identity;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpMethod;
import org.zstack.header.message.APIMessage;
import org.zstack.header.message.APIParam;
import org.zstack.header.message.APIReply;
import org.zstack.header.other.APILoginAuditor;
import org.zstack.header.rest.RestRequest;

import java.util.Map;

@SuppressCredentialCheck
@RestRequest(
        path = "/accounts/login",
        isAction = true,
        method = HttpMethod.PUT,
        responseClass = APILogInReply.class
)
public class APILogInByAccountMsg extends APISessionMessage implements APILoginAuditor {
    @APIParam
    private String accountName;
    @APIParam
    private String password;
    @APIParam(required = false)
    private String accountType;
    @APIParam(required = false)
    private String captchaUuid;
    @APIParam(required = false)
    private String verifyCode;
    @APIParam(required = false)
    private Map<String, String> clientInfo;


    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCaptchaUuid() {
        return captchaUuid;
    }

    public void setCaptchaUuid(String captchaUuid) {
        this.captchaUuid = captchaUuid;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Map<String, String> getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(Map<String, String> clientInfo) {
        this.clientInfo = clientInfo;
    }

    public static APILogInByAccountMsg __example__() {
        APILogInByAccountMsg msg = new APILogInByAccountMsg();

        msg.setAccountName("test");
        msg.setPassword("password");

        return msg;
    }

    @Override
    public LoginResult loginAudit(APIMessage msg, APIReply reply) {
        String clientIp = "";
        String clientBrowser = "";
        APILogInByAccountMsg amsg = (APILogInByAccountMsg) msg;
        Map<String, String> clientInfo = amsg.getClientInfo();
        if (clientInfo != null && clientInfo.size() > 0) {
            clientIp = StringUtils.isNotEmpty(clientInfo.get("clientIp")) ? clientInfo.get("clientIp") : "";
            clientBrowser = StringUtils.isNotEmpty(clientInfo.get("clientBrowser")) ? clientInfo.get("clientBrowser") : "";
        }
        String resourceUuid = reply.isSuccess() ? amsg.getSession().getUuid() : "";
        return new LoginResult(clientIp, clientBrowser, resourceUuid, SessionVO.class);
    }
}
