/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.web.servlet.view.velocity;

import java.io.StringWriter;
import java.util.Locale;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import org.apache.velocity.exception.ResourceNotFoundException;

import org.springframework.core.NestedIOException;

/**
 * VelocityLayoutView emulates the functionality offered by Velocity's
 * VelocityLayoutServlet to ease page composition from different templates.
 *
 * <p>The {@code url} property should be set to the content template
 * for the view, and the layout template location should be specified as
 * {@code layoutUrl} property. A view can override the configured
 * layout template location by setting the appropriate key (the default
 * is "layout") in the content template.
 *
 * <p>When the view is rendered, the VelocityContext is first merged with
 * the content template (specified by the {@code url} property) and
 * then merged with the layout template to produce the final output.
 *
 * <p>The layout template can include the screen content through a
 * VelocityContext variable (the default is "screen_content").
 * At runtime, this variable will contain the rendered content template.
 *
 * @author Darren Davison
 * @author Juergen Hoeller
 * @since 1.2
 * @see #setLayoutUrl
 * @see #setLayoutKey
 * @see #setScreenContentKey
 */
public class VelocityLayoutView extends VelocityToolboxView {

	/**
	 * The default {@link #setLayoutKey(String) layout key}.
	 */
	public static final String DEFAULT_LAYOUT_KEY = "layout";

	/**
	 * The default {@link #setScreenContentKey(String) screen content key}.
	 */
	public static final String DEFAULT_SCREEN_CONTENT_KEY = "screen_content";

	private String layoutUrl;

	private String layoutKey = DEFAULT_LAYOUT_KEY;

	private String screenContentKey = DEFAULT_SCREEN_CONTENT_KEY;


	/**
	 * 设置默认布局模板
	 */
	public void setLayoutUrl(String layoutUrl) {
		this.layoutUrl = layoutUrl;
	}

	/**
	 * Set the context key used to specify an alternate layout to be used instead
	 * of the default layout. Screen content templates can override the layout
	 * template that they wish to be wrapped with by setting this value in the
	 * template, for example:<br>
	 * {@code #set($layout = "MyLayout.vm" )}
	 * <p>Default key is {@link #DEFAULT_LAYOUT_KEY "layout"}, as illustrated above.
	 * @param layoutKey the name of the key you wish to use in your
	 * screen content templates to override the layout template
	 */
	public void setLayoutKey(String layoutKey) {
		this.layoutKey = layoutKey;
	}

	/**
	 * Set the name of the context key that will hold the content of
	 * the screen within the layout template. This key must be present
	 * in the layout template for the current screen to be rendered.
	 * <p>Default is {@link #DEFAULT_SCREEN_CONTENT_KEY "screen_content"}:
	 * accessed in VTL as {@code $screen_content}.
	 * @param screenContentKey the name of the screen content key to use
	 */
	public void setScreenContentKey(String screenContentKey) {
		this.screenContentKey = screenContentKey;
	}


	/**
	 * Overrides {@code VelocityView.checkTemplate()} to additionally check
	 * that both the layout template and the screen content template can be loaded.
	 * Note that during rendering of the screen content, the layout template
	 * can be changed which may invalidate any early checking done here.
	 */
	@Override
	public boolean checkResource(Locale locale) throws Exception {
		if (!super.checkResource(locale)) {
			return false;
		}

		if (this.layoutUrl != null) {
			try {
				// Check that we can get the template, even if we might subsequently get it again.
				getTemplate(this.layoutUrl);
			}
			catch (ResourceNotFoundException ex) {
				throw new NestedIOException("Cannot find Velocity template for URL [" + this.layoutUrl +
						"]: Did you specify the correct resource loader path?", ex);
			}
			catch (Exception ex) {
				throw new NestedIOException(
						"Could not load Velocity template for URL [" + this.layoutUrl + "]", ex);
			}
		}
		return true;
	}

	/**
	 * Overrides the normal rendering process in order to pre-process the Context,
	 * merging it with the screen template into a single value (identified by the
	 * value of screenContentKey). The layout template is then merged with the
	 * modified Context in the super class.
	 */
	@Override
	protected void doRender(Context context, HttpServletResponse response) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("Rendering screen content template [" + getUrl() + "]");
		}

		// 获取页面模板
		Template screenContentTemplate = getTemplate(getUrl());

		// 页面模板渲染
		StringWriter sw = new StringWriter();
		screenContentTemplate.merge(context, sw);

		// 查找使用的布局模板
		String layoutUrlToUse;
		if ((layoutUrlToUse = (String) context.get(this.layoutKey)) != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Screen content template has requested layout [" + layoutUrlToUse + "]");
			}
		} else if((layoutUrlToUse = this.layoutUrl) != null){
			// 未指定布局文件时，使用默认布局文件
			if (logger.isDebugEnabled()) {
				logger.debug("Screen content template has requested default layout [" + layoutUrlToUse + "]");
			}
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("Both the defined layout and the default layout is null");
			}
		}

		if (layoutUrlToUse == null) {
			// 无布局模板时，直接将页面内容输出到响应流
			response.getWriter().write(sw.toString());
		} else {
			// 有布局模板时，将页面内容作为布局模板中的一个变量
			context.put(this.screenContentKey, sw.toString());
			// 合并模板
			mergeTemplate(getTemplate(layoutUrlToUse), context, response);
		}
	}
}
