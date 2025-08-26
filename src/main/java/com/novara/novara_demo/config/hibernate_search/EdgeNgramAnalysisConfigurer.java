package com.novara.novara_demo.config.hibernate_search;

import org.hibernate.search.backend.lucene.analysis.LuceneAnalysisConfigurationContext;
import org.hibernate.search.backend.lucene.analysis.LuceneAnalysisConfigurer;

public class EdgeNgramAnalysisConfigurer implements LuceneAnalysisConfigurer {

    @Override
    public void configure(LuceneAnalysisConfigurationContext context) {
        context.analyzer("edgeNgram-english").custom()
                .tokenizer("standard")
                .tokenFilter("lowercase")
                .tokenFilter("asciiFolding")
                .tokenFilter("edgeNGram")
                .param("minGramSize", "3")
                .param("maxGramSize", "12");
    }
}
