package com.novara.novara_demo.config.hibernate_search;


import org.apache.lucene.analysis.ngram.NGramTokenFilter;
import org.hibernate.search.backend.lucene.analysis.LuceneAnalysisConfigurationContext;
import org.hibernate.search.backend.lucene.analysis.LuceneAnalysisConfigurer;

public class NgramAnalysisConfigurer implements LuceneAnalysisConfigurer {
    @Override
    public void configure(LuceneAnalysisConfigurationContext context) {
        context.analyzer("ngram-english").custom()
                .tokenizer("standard")
                .tokenFilter("lowercase")
                .tokenFilter("asciiFolding")
                .tokenFilter("NGram")
                .param("minGramSize", "3")
                .param("maxGramSize", "9");
    }
}
