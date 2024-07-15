/*
 * Copyright (c) 2002-2024, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.identitystore.v3.web.service;

import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.common.RequestAuthor;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.crud.IdentityChangeRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.crud.IdentityChangeResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.crud.UncertifyIdentityRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.merge.IdentityMergeRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.merge.IdentityMergeResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.task.IdentityTaskGetResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.task.IdentityTaskSearchRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.task.IdentityTaskSearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.task.IdentityTaskUpdateStatusRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.task.IdentityTaskUpdateStatusResponse;
import fr.paris.lutece.plugins.identitystore.web.exception.IdentityStoreException;

public class IdentityServiceExtended extends IdentityService
{

    public IdentityServiceExtended( )
    {
        super( );
    }

    public IdentityServiceExtended( final IIdentityTransportProvider transportProvider )
    {
        super( transportProvider );
    }

    /**
     * import an identity
     *
     * @param identityChange
     *            change to apply to identity
     * @param strClientCode
     *            client code of calling application
     * @param author
     *            the author of the request
     * @return identity filled according to application rights for user identified by connection id
     * @throws IdentityStoreException
     */
    public IdentityChangeResponse importIdentity( final IdentityChangeRequest identityChange, final String strClientCode, final RequestAuthor author )
            throws IdentityStoreException
    {
        return _transportProvider.importIdentity( identityChange, strClientCode, author );
    }

    /**
     * Merge two identities.
     *
     * @param identityMerge
     *            the request containing the master cuid, the secondary cuid, and a list of attribute to be taken from the secondary identity and put on the
     *            master identity.
     * @param strClientCode
     *            client code of calling application
     * @param author
     *            the author of the request
     * @return IdentityMergeResponse
     */
    public IdentityMergeResponse mergeIdentities( final IdentityMergeRequest identityMerge, final String strClientCode, final RequestAuthor author )
            throws IdentityStoreException
    {
        return _transportProvider.mergeIdentities( identityMerge, strClientCode, author );
    }

    /**
     * Unmerge two identities.
     *
     * @param identityMerge
     *            the request containing the master cuid, the secondary cuid
     * @param strClientCode
     *            client code of calling application
     * @param author
     *            the author of the request
     * @return IdentityMergeResponse
     */
    public IdentityMergeResponse unMergeIdentities( IdentityMergeRequest identityMerge, final String strClientCode, final RequestAuthor author )
            throws IdentityStoreException
    {
        return _transportProvider.unMergeIdentities( identityMerge, strClientCode, author );
    }

    /**
     * Dé-certification d'une identité.<br/>
     * Une identité ne pouvant pas posséder d'attributs non-certifiés, une dé-certification implique la certification de ses attributs avec le processus défini
     * par la property : <code>identitystore.identity.uncertify.processus</code> (par défaut : "dec", qui correspond au niveau le plus faible de certification
     * (auto-déclaratif))<br/>
     * Tous les attributs de l'identité seront dé-certifiés.
     *
     * @param strCustomerId
     *            the customer ID
     * @param strClientCode
     *            client code of calling application
     * @param author
     *            the author of the request
     * @return IdentityChangeResponse
     */
    public IdentityChangeResponse uncertifyIdentity( final String strCustomerId, final String strClientCode, final RequestAuthor author )
            throws IdentityStoreException
    {
        return _transportProvider.uncertifyIdentity( strCustomerId, strClientCode, author );
    }

    /**
     * Dé-certification d'une identité.<br/>
     * Une identité ne pouvant pas posséder d'attributs non-certifiés, une dé-certification implique la certification de ses attributs avec le processus défini
     * par la property : <code>identitystore.identity.uncertify.processus</code> (par défaut : "dec", qui correspond au niveau le plus faible de certification
     * (auto-déclaratif)).<br/>
     * Seuls les attributs présents dans la requête seront dé-certifiés. Si la requête est vide ou null, tous les attributs seront dé-certifiés.
     *
     * @param request
     *            the uncertify request
     * @param strCustomerId
     *            the customer ID
     * @param strClientCode
     *            client code of calling application
     * @param author
     *            the author of the request
     * @return IdentityChangeResponse
     */
    public IdentityChangeResponse uncertifyIdentity(final UncertifyIdentityRequest request, final String strCustomerId, final String strClientCode, final RequestAuthor author)
            throws IdentityStoreException
    {
        return _transportProvider.uncertifyIdentity( request, strCustomerId, strClientCode, author );
    }

    /**
     * Update the {@link fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.task.IdentityTaskStatusType} of the task.
     * @param request the task status update request
     * @param taskCode the code of the task returned at creation time
     * @param strClientCode the client code calling the request
     * @param author the author of the request
     * @return an {@link IdentityTaskUpdateStatusResponse}
     * @throws IdentityStoreException in case of error
     */
    public IdentityTaskUpdateStatusResponse updateIdentityTaskStatus(final String taskCode, final IdentityTaskUpdateStatusRequest request, final String strClientCode, final RequestAuthor author ) throws IdentityStoreException
    {
        return _transportProvider.updateIdentityTaskStatus( taskCode, request, strClientCode, author );
    }

    /**
     * Get the task.
     * @param taskCode the code of the task returned at creation time
     * @param strClientCode the client code calling the request
     * @param author the author of the request
     * @return an {@link IdentityTaskGetResponse}
     * @throws IdentityStoreException in case of error
     */
    public IdentityTaskGetResponse getIdentityTask( final String taskCode, final String strClientCode, final RequestAuthor author ) throws IdentityStoreException
    {
        return _transportProvider.getIdentityTaskList( taskCode, strClientCode, author );
    }



    /**
     * Search for tasks.
     * @param request the search request
     * @param strClientCode the client code calling the request
     * @param author the author of the request
     * @return an {@link IdentityTaskSearchResponse}
     * @throws IdentityStoreException in case of error
     */
    public IdentityTaskSearchResponse searchIdentityTasks(final IdentityTaskSearchRequest request, final String strClientCode, final RequestAuthor author ) throws IdentityStoreException
    {
        return _transportProvider.searchIdentityTasks( request, strClientCode, author );
    }

}
