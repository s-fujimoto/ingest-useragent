/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.ingest.useragent;

import is.tagomor.woothee.Classifier;
import org.elasticsearch.ingest.AbstractProcessor;
import org.elasticsearch.ingest.AbstractProcessorFactory;
import org.elasticsearch.ingest.IngestDocument;

import java.util.Map;
import static org.elasticsearch.ingest.ConfigurationUtils.readStringProperty;

public final class UserAgentProcessor extends AbstractProcessor {

    public static final String TYPE = "useragent";
    private final String field;
    private final String targetField;

    UserAgentProcessor(String tag, String field, String targetField) {
        super(tag);
        this.field = field;
        this.targetField = targetField;
    }

    public void execute(IngestDocument ingestDocument) {
        String userAgent = ingestDocument.getFieldValue(field, String.class);
        Map<String, String> u = Classifier.parse(userAgent);

        ingestDocument.setFieldValue(this.targetField, u);
    }

    @Override
    public String getType() {
        return null;
    }

    public static final class Factory extends AbstractProcessorFactory<UserAgentProcessor> {
        public Factory() {}

        public UserAgentProcessor doCreate(String processorTag, Map<String, Object> config) throws Exception {
            String userAgentField = readStringProperty(TYPE, processorTag, config, "field");
            String targetField = readStringProperty(TYPE, processorTag, config, "targetField", "useragent");
            return new UserAgentProcessor(processorTag, userAgentField, targetField);
        }
    }
}
