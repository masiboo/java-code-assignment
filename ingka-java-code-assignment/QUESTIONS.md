# Questions

Here we have 3 questions related to the code base for you to answer. It is not about right or wrong, but more about what's the reasoning behind your decisions.

1. In this code base, we have some different implementation strategies when it comes to database access layer and manipulation. If you would maintain this code base, would you refactor any of those? Why?

**Answer:**
```txt
The project currently mixes the Active Record pattern (in Product and Store entities) with the Repository pattern (in the warehouses package)  

* Refactor: I would move everything to the Panache Repository pattern.
* Why: Mixing these patterns is confusing for developers. Repositories provide better separation of concerns, make it easier to mock data for unit tests, and keep the domain entities "clean" from infrastructure logic.  
  
By implementing these changes, the codebase would become significantly more predictable, easier to test, and robust against future requirement changes.

```
----
2. When it comes to API spec and endpoints handlers, we have an Open API yaml file for the `Warehouse` API from which we generate code, but for the other endpoints - `Product` and `Store` - we just coded directly everything. What would be your thoughts about what are the pros and cons of each approach and what would be your choice?

**Answer:**
```txt
If I were maintaining this project, I would move the Product and Store to OpenAPI/Swagger which is the API-First approach. Here is why:  

1. Consistency is King: Having two different ways to define APIs in one project is confusing. Standardizing on one approach makes the codebase more predictable.

2. Validation by Default: OpenAPI generators often include validation logic (like @NotNull or @Pattern) based on the YAML. This reduces the manual "if-null" checks in your Resource classes (like the ones I fixed in StoreResource).

3. Future-Proofing: If we ever want to generate a Client SDK (in TypeScript, Python, etc.), having a clean, verified OpenAPI spec for all endpoints makes it a one-click process.

4. Better Design: Writing the YAML forces us to think about the API as a service for a consumer, rather than just an export of our database tables. This usually leads to cleaner, more RESTful designs.    

For Product and Store was Code-First is great for tiny prototypes or internal-only tools, where Warehouse was defined in Open API yaml file that is API-First is the industry standard for professional, scalable systems.
```