package com.metasoft.boot;

import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;

public class SiteMeshFilter extends ConfigurableSiteMeshFilter {
	@Override
	protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
		// Assigning default decorator if no path specific decorator found
		builder.addDecoratorPath("/*", "/WEB-INF/sitemesh/model-dev.jsp");
				// Exclude few paths from decoration.
				//.addExcludedPath("/html/*");
	}
}