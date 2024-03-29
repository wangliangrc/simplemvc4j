/****************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one   *
 * or more contributor license agreements.  See the NOTICE file *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The ASF licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http://www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 ****************************************************************/

package org.apache.james.mime4j.stream;

/**
 * Enumeration of parsing modes.
 */
public enum RecursionMode {

    /**
     * Recursively parse every <code>message/rfc822</code> part
     */
    M_RECURSE,
    /**
     * Do not recurse <code>message/rfc822</code> parts
     */
    M_NO_RECURSE,
    /**
     * Parse into raw entities
     */
    M_RAW,
    /**
     * Do not recurse <code>message/rfc822</code> parts and treat multiparts as
     * a single flat body.
     */
    M_FLAT

}
