![](https://dev.lutece.paris.fr/jenkins/buildStatus/icon?job=library-identitystore-extended-deploy)
[![Alerte](https://dev.lutece.paris.fr/sonar/api/project_badges/measure?project=fr.paris.lutece.plugins%3Alibrary-identitystore-extended&metric=alert_status)](https://dev.lutece.paris.fr/sonar/dashboard?id=fr.paris.lutece.plugins%3Alibrary-identitystore-extended)
[![Line of code](https://dev.lutece.paris.fr/sonar/api/project_badges/measure?project=fr.paris.lutece.plugins%3Alibrary-identitystore-extended&metric=ncloc)](https://dev.lutece.paris.fr/sonar/dashboard?id=fr.paris.lutece.plugins%3Alibrary-identitystore-extended)
[![Coverage](https://dev.lutece.paris.fr/sonar/api/project_badges/measure?project=fr.paris.lutece.plugins%3Alibrary-identitystore-extended&metric=coverage)](https://dev.lutece.paris.fr/sonar/dashboard?id=fr.paris.lutece.plugins%3Alibrary-identitystore-extended)

# Library Identitystore

## Introduction

This library provides services to communicate with Identity Store REST API.

This is an extension of the Identitystore Librairy, providing additionnal and more advanced services.

Further information can be found on the [Identitystore Librairy official wiki](https://lutece.paris.fr/support/wiki/gru-library-identitystore-v3.html).

## Services

All services provided by the parent librairy are available in this extended version. To be able to access the extended functions of `IdentityService` and `ServiceContractService` , just use `IdentityServiceExtended` and `ServiceContractServiceExtended` instead. Their configuration and usage remain the same.

A new service `fr.paris.lutece.plugins.identitystore.v3.web.service.ReferentialService` is available, which provides methods to access the referential data.

These methods are implemented in the rest transport `fr.paris.lutece.plugins.identitystore.v3.web.rs.service.ReferentialTransportRest` .

It requires an implementation of `fr.paris.lutece.plugins.identitystore.v3.web.service.IHttpTransportProvider` to define the HTTP transport. Two implementations of this interface are provided in the library :
 
*  `fr.paris.lutece.plugins.identitystore.v3.web.rs.service.HttpAccessTransport` , which uses simple requests
*  `fr.paris.lutece.plugins.identitystore.v3.web.rs.service.HttpApiManagerAccessTransport` , which calls endpoints through an ApiManager in order to secure requests to the API (by using tokens)


Both implementations require URL definition of the Identity Store service end point.
 
*  `apiEndPointUrl` , the URL to the Identity store endpoint.


When using `HttpApiManagerAccessTransport` , two extra parameters are required:
 
*  `accessManagerEndPointUrl` , the URL to the API manager token endpoint.
*  `accessManagerCredentials` , the credentials to access the API manager token endpoint.


## Configuration using Spring context

First, define the bean for the HTTP transport you want to use:
 
* set the property for the URL of the Identity Store service end point
* set other properties if using the HTTP transport `HttpApiManagerAccessTransport` 


Then, define the bean for the rest transport `IdentityTransportRest` :
 
* as a constructor argument, refer to the bean for HTTP transport


Then, define the bean for the service `IdentityServiceExtended` :
 
* as a constructor argument, refer to the bean for rest transport


Here is an example of Spring configuration with the HTTP transport `HttpAccessTransport` :
```

<!-- library identitystore -->
<!-- IHttpTransportProvider declarations -->
<bean id="identitystore.httpAccessTransport" class="fr.paris.lutece.plugins.identitystore.v3.web.rs.service.HttpAccessTransport" >
    <property name="apiEndPointUrl">
        <value>${identitydesk.identitystore.apiEndPointUrl}</value>
    </property>
</bean>

<bean id="identity.restTransport.httpAccess" class="fr.paris.lutece.plugins.identitystore.v3.web.rs.service.IdentityTransportRest">
    <constructor-arg ref="identitystore.httpAccessTransport"/>
</bean>

<!-- IdentityServiceExtended impl -->
<bean id="identity.identityServiceExtended" class="fr.paris.lutece.plugins.identitystore.v3.web.service.IdentityServiceExtended">
    <constructor-arg ref="identity.restTransport.httpAccess"/>
</bean>
                        
```


Here is an example of Spring configuration with the HTTP transport `HttpApiManagerAccessTransport` :
```

<!-- library identitystore -->
<!-- IHttpTransportProvider declarations -->
<bean id="identitystore.httpAccessTransport" class="fr.paris.lutece.plugins.identitystore.v3.web.rs.service.HttpApiManagerAccessTransport">
    <property name="apiEndPointUrl">
        <value>${myplugin.identitystore.ApiEndPointUrl}</value>
    </property>
    <property name="accessManagerEndPointUrl">
        <value>${myplugin.identitystore.accessManagerEndPointUrl}</value>
    </property>
    <property name="accessManagerCredentials">
        <value>${myplugin.identitystore.accessManagerCredentials}</value>
    </property>
</bean>

<bean id="identity.restTransport.httpAccess" class="fr.paris.lutece.plugins.identitystore.v3.web.rs.service.IdentityTransportRest">
    <constructor-arg ref="identitystore.httpAccessTransport"/>
</bean>

<!-- IdentityServiceExtended impl -->
<bean id="identity.identityServiceExtended" class="fr.paris.lutece.plugins.identitystore.v3.web.service.IdentityServiceExtended">
    <constructor-arg ref="identity.restTransport.httpAccess"/>
</bean>
                        
```


You can finally access your bean in the java code as follows:
```

import fr.paris.lutece.plugins.identitystore.v3.web.service.IdentityServiceExtended;
...
private IdentityServiceExtended _identityServiceExt = SpringContextService.getBean( "identity.identityServiceExtended" );
                        
```


## Configuration in Java code

The service can directly be created in the Java code. Here is an example with the HTTP transport `HttpApiManagerAccessTransport` (the same mechanism can be applied for the HTTP transport `HttpAccessTransport` ).

First, define the following keys in a properties file:
```

myplugin.identitystore.ApiEndPointUrl=http://mydomain.com/url/to/apimanager/api/identitystore
myplugin.identitystore.accessManagerEndPointUrl=http://mydomain.com/url/to/apimanager/token
myplugin.identitystore.accessManagerCredentials=your_private_key
                        
```


Then, add the following code in the Java code:
```

...
private static final String PROPERTY_GRU_API_ENDPOINT = "myplugin.identitystore.ApiEndPointUrl";
private static final String PROPERTY_GRU_AM_ENDPOINT = "myplugin.identitystore.accessManagerEndPointUrl";
private static final String PROPERTY_GRU_AM_CREDENTIALS = "myplugin.identitystore.accessManagerCredentials";
...

HttpApiManagerAccessTransport httpApiManagerAccessTransport = new HttpApiManagerAccessTransport( )

httpApiManagerAccessTransport.setApiEndPointUrl( AppPropertiesService.getProperty( PROPERTY_GRU_API_ENDPOINT ) );
httpApiManagerAccessTransport.setAccessManagerEndPointUrl( AppPropertiesService.getProperty( PROPERTY_GRU_AM_ENDPOINT ) );
httpApiManagerAccessTransport.setAccessManagerCredentials( AppPropertiesService.getProperty( PROPERTY_GRU_AM_CREDENTIALS ) );

IdentityTransportRest  identityTransport = new IdentityTransportRest( httpApiManagerAccessTransport );

final IdentityServiceExtended identityServiceExt = new IdentityServiceExtended( identityTransport );
...
                        
```



[Maven documentation and reports](https://dev.lutece.paris.fr/plugins/library-identitystore-extended/)



 *generated by [xdoc2md](https://github.com/lutece-platform/tools-maven-xdoc2md-plugin) - do not edit directly.*