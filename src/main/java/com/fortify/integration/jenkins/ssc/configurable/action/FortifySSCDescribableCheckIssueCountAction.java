/*******************************************************************************
 * (c) Copyright 2017 EntIT Software LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a 
 * copy of this software and associated documentation files (the 
 * "Software"), to deal in the Software without restriction, including without 
 * limitation the rights to use, copy, modify, merge, publish, distribute, 
 * sublicense, and/or sell copies of the Software, and to permit persons to 
 * whom the Software is furnished to do so, subject to the following 
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be included 
 * in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY 
 * KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE 
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR 
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF 
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN 
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS 
 * IN THE SOFTWARE.
 ******************************************************************************/
package com.fortify.integration.jenkins.ssc.configurable.action;

import java.io.IOException;
import java.io.PrintStream;

import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

import com.fortify.client.ssc.api.SSCIssueAPI;
import com.fortify.client.ssc.connection.SSCAuthenticatingRestConnection;
import com.fortify.integration.jenkins.ssc.FortifySSCGlobalConfiguration;
import com.fortify.integration.jenkins.ssc.configurable.FortifySSCDescribableApplicationAndVersionName;
import com.fortify.integration.jenkins.util.ModelHelper;

import hudson.AbortException;
import hudson.EnvVars;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.Describable;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.util.ListBoxModel;

// TODO Add support for selecting SSC filterset
public class FortifySSCDescribableCheckIssueCountAction extends AbstractFortifySSCDescribableAction {
	private static final long serialVersionUID = 1L;
	private String searchString = "";
	private String operator = "GT";
	private int number = 0;
	
	/**
	 * Default constructor
	 */
	@DataBoundConstructor
	public FortifySSCDescribableCheckIssueCountAction() {}
	
	/**
	 * Copy constructor
	 * @param other
	 */
	public FortifySSCDescribableCheckIssueCountAction(FortifySSCDescribableCheckIssueCountAction other) {
		if ( other != null ) {
			setSearchString(other.getSearchString());
			setOperator(other.getOperator());
			setNumber(other.getNumber());
		}
	}
	
	public String getSearchString() {
		return getSearchString(null);
	}
	
	public String getSearchString(PrintStream log) {
		return getPropertyValueOrDefaultValueIfOverrideDisallowed(log, "searchString", searchString);
	}

	@DataBoundSetter
	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	public String getOperator() {
		return getOperator(null);
	}
	
	public String getOperator(PrintStream log) {
		return getPropertyValueOrDefaultValueIfOverrideDisallowed(log, "operator", operator);
	}

	@DataBoundSetter
	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	public int getNumber() {
		return getNumber(null);
	}

	public int getNumber(PrintStream log) {
		return getPropertyValueOrDefaultValueIfOverrideDisallowed(log, "number", number);
	}

	@DataBoundSetter
	public void setNumber(int number) {
		this.number = number;
	}

	@Override
	public void perform(FortifySSCDescribableApplicationAndVersionName applicationAndVersionNameJobConfig, Run<?, ?> run,
			FilePath workspace, Launcher launcher, TaskListener listener) throws InterruptedException, IOException {
		PrintStream log = listener.getLogger();
		EnvVars env = run.getEnvironment(listener);
		int numberToCompare = getNumber(log);
		String operator = getOperator(log);
		String searchString = getSearchString(log);
		
		SSCAuthenticatingRestConnection conn = FortifySSCGlobalConfiguration.get().conn();
		final String applicationVersionId = applicationAndVersionNameJobConfig.getApplicationVersionId(env, log);
		int numberOfIssues = conn.api(SSCIssueAPI.class).queryIssues(applicationVersionId)
			.paramFilter(searchString).maxResults(number+1).paramFields("id").useCache(false)
			.build().getAll().size();
		if ( !compare(numberOfIssues, operator, numberToCompare) ) {
			throw new AbortException("Issue count check "+numberOfIssues+" "+operator+" "+numberToCompare+" failed");
		}
	}

	private boolean compare(int value1, String operator, int value2) {
		switch (operator) {
			case "<":
				return value1 < value2;
			case "=":
				return value1 == value2;
			case ">":
				return value1 > value2;
			default:
				throw new IllegalArgumentException("Illegal compare operator '"+operator+"'");
		}
	}
	
	@Symbol("checkIssueCount")
	@Extension
	public static final class FortifySSCDescriptorCheckIssueCount extends AbstractFortifySSCDescriptorAction {
		static final String DISPLAY_NAME = "Check Issue Count";

		@Override
		public FortifySSCDescribableCheckIssueCountAction createDefaultInstanceWithConfiguration() {
			return new FortifySSCDescribableCheckIssueCountAction(getDefaultConfiguration());
		}
		
		@Override
		public FortifySSCDescribableCheckIssueCountAction createDefaultInstance() {
			return new FortifySSCDescribableCheckIssueCountAction();
		}
		
		@Override
		protected FortifySSCDescribableCheckIssueCountAction getDefaultConfiguration() {
			return (FortifySSCDescribableCheckIssueCountAction)super.getDefaultConfiguration();
		}
		
		@Override
		protected Class<? extends Describable<?>> getGlobalConfigurationTargetType() {
			return FortifySSCDescribableCheckIssueCountAction.class;
		}
		
		@Override
		public String getDisplayName() {
			// TODO Internationalize this
			return DISPLAY_NAME;
		}
		
		@Override
		public int getOrder() {
			return 500;
		}
		
		public ListBoxModel doFillOperatorItems() {
			final ListBoxModel items = ModelHelper.createListBoxModelWithNotSpecifiedOption();
			items.add("<");
			items.add("=");
			items.add(">");
			return items;
		}
    }
}
