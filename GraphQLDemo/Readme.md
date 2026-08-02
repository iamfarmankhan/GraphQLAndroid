## A mutation in GraphQL is a specific type of operation used to modify server-side data, such as creating, updating, or deleting records.

## Queries follow a request-response pattern and Mutations follow a write-then-respond pattern, Subscriptions use a push model. When an event occurs on the server, the server automatically pushes the updated data to the client.

## difference between type and input? type is Output: Server → Client. Input: Client → Server

## In modern versions, Apollo uses POST by default for queries and mutations. You can customize this behavior per-request using the .httpMethod() extension modifier:

```Kotlin
       val response = apolloClient.query(MyQuery())
                        .httpMethod(HttpMethod.Get)
                         .execute()
```

## When you execute a query or mutation and need access to HTTP-specific data like status codes or network headers, access the execution context directly from the returned ApolloResponse using the HttpInfo key:



```
val response = apolloClient.query(MyQuery()).execute()
val httpInfo = response.executionContext[HttpInfo]
val statusCode = httpInfo?.statusCode // e.g., 200
val contentType = httpInfo?.headers?.firstOrNull { it.name == "Content-Type" }?.value
```

## you can define scheme in in JSON instead of .graphqls but it is verbose,lengthy and complex
