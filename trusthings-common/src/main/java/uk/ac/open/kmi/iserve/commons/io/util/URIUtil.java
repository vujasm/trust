package uk.ac.open.kmi.iserve.commons.io.util;

/*
 * #%L
 * itrust-common
 * %%
 * Copyright (C) 2014 INNOVA S.p.A
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


/*
 * Copyright (c) 2013. Knowledge Media Institute - The Open University
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

//package uk.ac.open.kmi.iserve.commons.io.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class URIUtil {

    public static String getLocalName(URI uri) {
        if (uri == null)
            return null;

        return getLocalName(uri.toASCIIString());
    }

    public static String getLocalName(URL url) {
        if (url == null)
            return null;
        return getLocalName(url.toString());
    }

    public static String getLocalName(String uriString) {

        if (uriString == null || uriString.equals(""))
            return uriString;

        int len = uriString.length();
        if (uriString.endsWith("/")) {
            uriString = uriString.substring(0, len - 1);
        }
        int idx = uriString.indexOf('^');
        if (idx >= 0) {
            uriString = uriString.substring(0, idx);
        }
        int localNameIdx = getLocalNameIndex(uriString);
        if (localNameIdx == 0) {
            // It's an absolute name
            return uriString;
        }

        return uriString.substring(localNameIdx);
    }

    private static int getLocalNameIndex(String uri) {
        int separatorIdx = uri.indexOf('#');

        if (separatorIdx < 0) {
            separatorIdx = uri.lastIndexOf('/');
        }

        if (separatorIdx < 0) {
            separatorIdx = uri.lastIndexOf(':');
        }

        if (separatorIdx < 0) {
            throw new IllegalArgumentException("No separator character founds in URI: " + uri);
        }

        return separatorIdx + 1;
    }

    public static URI getNameSpace(URI uri) throws URISyntaxException {
        return getNameSpace(uri.toASCIIString());
    }

    public static URI getNameSpace(String uriString) throws URISyntaxException {
        int localNameIdx = getLocalNameIndex(uriString);
        return new URI(uriString.substring(0, localNameIdx - 1));
    }

    public static String generateItemLabel(URL itemUrl) {
        return generateItemLabel(null, itemUrl);
    }

    public static String generateItemLabel(String prefix, URL itemUrl) {

        // Check the input and exit immediately if null
        if (itemUrl == null) {
            return null;
        }

        String result = null;
        if (prefix != null && !prefix.isEmpty()) {
            result = prefix + "." + URIUtil.getLocalName(itemUrl);
        } else {
            result = URIUtil.getLocalName(itemUrl);
        }

        return result;
    }

    /**
     * Replace the URIs with the newBase and make them instead slash URIs to allow finer grain access and modification
     * of resources
     *
     * @param originalUri
     * @param newBaseUri
     * @return
     * @throws URISyntaxException
     */
    public static URI replaceNamespace(URI originalUri, URI newBaseUri) throws URISyntaxException {
//        return new URI(newBaseUri.getScheme(), newBaseUri.getSchemeSpecificPart(), getLocalName(originalUri));
//        return new URI(newBaseUri.getScheme(), newBaseUri.getPath() + "/" + getLocalName(originalUri), null);
        // In case of blank nodes, don't try to generate a URI
        if (originalUri == null) {
            return originalUri;
        }

        return new URI(newBaseUri.getScheme(), newBaseUri.getUserInfo(), newBaseUri.getHost(), newBaseUri.getPort(),
                newBaseUri.getPath() + "/" + getLocalName(originalUri), null, null);
    }

}
