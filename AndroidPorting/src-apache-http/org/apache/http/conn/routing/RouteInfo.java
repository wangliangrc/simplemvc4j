/*
 * $HeadURL: http://svn.apache.org/repos/asf/httpcomponents/httpclient/trunk/module-client/src/main/java/org/apache/http/conn/routing/RouteInfo.java $
 * $Revision: 652200 $
 * $Date: 2008-04-29 17:22:43 -0700 (Tue, 29 Apr 2008) $
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

package org.apache.http.conn.routing;

import java.net.InetAddress;

import org.apache.http.HttpHost;

/**
 * Read-only interface for route information.
 * 
 * @author <a href="mailto:rolandw at apache.org">Roland Weber</a>
 * 
 * 
 *         <!-- empty lines to avoid svn diff problems -->
 * @version $Revision: 652200 $
 * 
 * @since 4.0
 */
public interface RouteInfo {

    /**
     * The tunnelling type of a route. Plain routes are established by
     * connecting to the target or the first proxy. Tunnelled routes are
     * established by connecting to the first proxy and tunnelling through all
     * proxies to the target. Routes without a proxy cannot be tunnelled.
     */
    public enum TunnelType {
        PLAIN, TUNNELLED
    }

    /**
     * The layering type of a route. Plain routes are established by connecting
     * or tunnelling. Layered routes are established by layering a protocol such
     * as TLS/SSL over an existing connection. Protocols can only be layered
     * over a tunnel to the target, or or over a direct connection without
     * proxies. <br/>
     * Layering a protocol over a direct connection makes little sense, since
     * the connection could be established with the new protocol in the first
     * place. But we don't want to exclude that use case.
     */
    public enum LayerType {
        PLAIN, LAYERED
    }

    /**
     * Obtains the target host.
     * 
     * @return the target host
     */
    HttpHost getTargetHost();

    /**
     * Obtains the local address to connect from.
     * 
     * @return the local address, or <code>null</code>
     */
    InetAddress getLocalAddress();

    /**
     * Obtains the number of hops in this route. A direct route has one hop. A
     * route through a proxy has two hops. A route through a chain of <i>n</i>
     * proxies has <i>n+1</i> hops.
     * 
     * @return the number of hops in this route
     */
    int getHopCount();

    /**
     * Obtains the target of a hop in this route. The target of the last hop is
     * the {@link #getTargetHost target host}, the target of previous hops is
     * the respective proxy in the chain. For a route through exactly one proxy,
     * target of hop 0 is the proxy and target of hop 1 is the target host.
     * 
     * @param hop
     *            index of the hop for which to get the target, 0 for first
     * 
     * @return the target of the given hop
     * 
     * @throws IllegalArgumentException
     *             if the argument is negative or not less than
     *             {@link #getHopCount getHopCount()}
     */
    HttpHost getHopTarget(int hop);

    /**
     * Obtains the first proxy host.
     * 
     * @return the first proxy in the proxy chain, or <code>null</code> if this
     *         route is direct
     */
    HttpHost getProxyHost();

    /**
     * Obtains the tunnel type of this route. If there is a proxy chain, only
     * end-to-end tunnels are considered.
     * 
     * @return the tunnelling type
     */
    TunnelType getTunnelType();

    /**
     * Checks whether this route is tunnelled through a proxy. If there is a
     * proxy chain, only end-to-end tunnels are considered.
     * 
     * @return <code>true</code> if tunnelled end-to-end through at least one
     *         proxy, <code>false</code> otherwise
     */
    boolean isTunnelled();

    /**
     * Obtains the layering type of this route. In the presence of proxies, only
     * layering over an end-to-end tunnel is considered.
     * 
     * @return the layering type
     */
    LayerType getLayerType();

    /**
     * Checks whether this route includes a layered protocol. In the presence of
     * proxies, only layering over an end-to-end tunnel is considered.
     * 
     * @return <code>true</code> if layered, <code>false</code> otherwise
     */
    boolean isLayered();

    /**
     * Checks whether this route is secure.
     * 
     * @return <code>true</code> if secure, <code>false</code> otherwise
     */
    boolean isSecure();

} // interface RouteInfo
