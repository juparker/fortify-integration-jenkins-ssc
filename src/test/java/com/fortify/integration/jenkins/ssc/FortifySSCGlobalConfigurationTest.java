package com.fortify.integration.jenkins.ssc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.RestartableJenkinsRule;

import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class FortifySSCGlobalConfigurationTest {

    @Rule
    public RestartableJenkinsRule rr = new RestartableJenkinsRule();

    /**
     * Tries to exercise enough code paths to catch common mistakes:
     * <ul>
     * <li>missing {@code load}
     * <li>missing {@code save}
     * <li>misnamed or absent getter/setter
     * <li>misnamed {@code textbox}
     * </ul>
     */
    @Test
    public void uiAndStorage() {
    	final String sscUrl = "https://user:password@somehost.com/ssc";
        rr.then(r -> {
            assertNull("not set initially", FortifySSCGlobalConfiguration.get().getSscUrl());
            HtmlForm config = r.createWebClient().goTo("configure").getFormByName("config");
            HtmlTextInput textbox = config.getInputByName("_.sscUrl");
            textbox.setText(sscUrl);
            r.submit(config);
            assertEquals("global config page let us edit it", sscUrl, FortifySSCGlobalConfiguration.get().getSscUrl());
        });
        rr.then(r -> {
            assertEquals("still there after restart of Jenkins", sscUrl, FortifySSCGlobalConfiguration.get().getSscUrl());
        });
    }

}
