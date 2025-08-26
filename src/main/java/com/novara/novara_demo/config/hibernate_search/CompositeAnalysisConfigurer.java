package com.novara.novara_demo.config.hibernate_search;

import org.hibernate.search.backend.lucene.analysis.LuceneAnalysisConfigurationContext;
import org.hibernate.search.backend.lucene.analysis.LuceneAnalysisConfigurer;

public class CompositeAnalysisConfigurer implements LuceneAnalysisConfigurer {
    @Override
    public void configure(LuceneAnalysisConfigurationContext context) {
        new NgramAnalysisConfigurer().configure(context);
        new EdgeNgramAnalysisConfigurer().configure(context);
    }
}
