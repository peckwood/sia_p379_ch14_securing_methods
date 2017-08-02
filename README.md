What we did in chapter 9(Securing web applications) was securing the **web layer,** now we secure the **methods**

------

3 kinds of security annotations

- Spring Security’s own @Secured
  - if securedEnabled is set to true, pointcut is created to wrap methods annotated with @Secured
  - `@Secured ({"ROLE_USER", "ROLE_ADMIN"})` on method means only user or admin can invoke it
  - throws Spring Security's exception if not passed
  - Spring-specific
- JSR-250’s @RolesAllowed
  - equivalent to @Security, most differences are political
  - may be different in other frameworks
  - `@EnableGlobalMethodSecurity(jsr250Enabled=true)`
- Expression-driven annotations, with @PreAuthorize, @PostAuthorize, @PreFilter, and @PostFilter
  - more powerful than the above 2 (can specify beyond ROLES)

WebSecurityConfigurerAdapter vs GlobalMethodSecurityConfiguration 

secures: web layer | methods

both have method configure(AuthenticationManagerBuilder auth)

---

### Demo Code

@Secured and @JSR250 are basically the same thing, there are only "political" differences. There may be differences when @JSR250 is used on another framework other than Spring.

For a demo of custom permission evaluator, see files:

- `/main/java/spittr/security/SpittlePermissionEvaluator.java`: definition class
- `/main/java/spittr/config/ExpressionSecuredConfig.java`: How is it plugged into Spring's method security expression handler: by replacing `DefaultMethodSecurityExpressionHandler `
- `/main/java/spittr/service/ExpressionSecuredSpittleService.java`'s version2 of `deleteSpittles`: how is it applied
- `/test/java/spittr/service/ExpressionSecuredSpittleServiceTest.java`'s `testSecuredMethod__DeleteOwnSpittles`: how is it tested

