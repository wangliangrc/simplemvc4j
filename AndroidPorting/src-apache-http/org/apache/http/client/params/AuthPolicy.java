/*
 * $HeadURL: http://svn.apache.org/repos/asf/httpcomponents/httpclient/trunk/module-client/src/main/java/org/apache/http/client/params/AuthPolicy.java $
 * $Revision: 534839 $
 * $Date: 2007-05-03 06:03:41 -0700 (Thu, 03 May 2007) $
 *
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.http.client.params;

public final class AuthPolicy {

    private AuthPolicy() {
        super();
    }

    /**
     * The NTLM scheme is a proprietary Microsoft Windows Authentication
     * protocol (considered to be the most secure among currently supported
     * authentication schemes).
     */
    public static final String NTLM = "NTLM";

    /**
     * Digest authentication scheme as defined in RFC2617.
     */
    public static final String DIGEST = "Digest";

    /**
     * Basic authentication scheme as defined in RFC2617 (considered inherently
     * insecure, but most widely supported)
     */
    public static final String BASIC = "Basic";

}
