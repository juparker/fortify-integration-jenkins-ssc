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
package com.fortify.integration.jenkins.ssc.describable.action;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

import com.fortify.integration.jenkins.multiaction.AbstractMultiActionConfigurableDescribable;
import com.fortify.integration.jenkins.ssc.describable.action.FortifySSCDescribableUploadFPRAction.FortifySSCDescriptorUploadFPRAction;

import hudson.Extension;
import hudson.model.Describable;

public class FortifySSCDescribableUploadFPRActionGlobalConfiguration extends AbstractFortifySSCDescribableActionGlobalConfiguration<FortifySSCDescribableUploadFPRActionGlobalConfiguration> {
	private static final long serialVersionUID = 1L;
	private FortifySSCDescribableUploadFPRAction target;
	
	@DataBoundConstructor
	public FortifySSCDescribableUploadFPRActionGlobalConfiguration() {}
	
	public FortifySSCDescribableUploadFPRAction getTarget() {
		return target;
	}

	@DataBoundSetter
	public void setTarget(FortifySSCDescribableUploadFPRAction target) {
		this.target = target;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Class<FortifySSCDescribableUploadFPRAction> getTargetType() {
		return FortifySSCDescribableUploadFPRAction.class;
	}
	
	@Extension
	public static final class FortifySSCDescriptorUploadFPRActionGlobalConfiguration extends AbstractFortifySSCDescriptorGlobalConfiguration<FortifySSCDescribableUploadFPRActionGlobalConfiguration> {        
        @Override
        public FortifySSCDescribableUploadFPRActionGlobalConfiguration createDefaultInstance() {
        	return new FortifySSCDescribableUploadFPRActionGlobalConfiguration();
        }
        
        @Override
        protected Class<? extends Describable<?>> getTargetType() {
        	return FortifySSCDescribableUploadFPRAction.class;
        }
        
        @Override
        public String getDisplayName() {
        	return FortifySSCDescriptorUploadFPRAction.DISPLAY_NAME;
        }
    }
}