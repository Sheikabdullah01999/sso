package com.grootan.sso.controller;


import com.grootan.sso.model.UserInfo;
import com.grootan.sso.repository.UserRepo;
import com.grootan.sso.serivice.UserService;
import jdk.jfr.Unsigned;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
public class TokenController<T> {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepo userRepo;

    @Autowired
    EntityManager entityManager;

//    @RequestMapping("/")
//    private StringBuffer getOauth2LoginInfo(Principal user) {
//
//        StringBuffer protectedInfo = new StringBuffer();
//
//        OAuth2AuthenticationToken authToken = ((OAuth2AuthenticationToken) user);
//        OAuth2AuthorizedClient authClient = this.authorizedClientService.loadAuthorizedClient(authToken.getAuthorizedClientRegistrationId(), authToken.getName());
//        if (authToken.isAuthenticated()) {
//
//            Map<String, Object> userAttributes = ((DefaultOAuth2User) authToken.getPrincipal()).getAttributes();
//
//            String userToken = authClient.getAccessToken().getTokenValue();
//            protectedInfo.append("Welcome, " + userAttributes.get("name") + "<br><br>");
//            protectedInfo.append("e-mail: " + userAttributes.get("email") + "<br><br>");
//            protectedInfo.append("Access Token: " + userToken + "<br><br>");
//        } else {
//            protectedInfo.append("NA");
//        }
//        return protectedInfo;
//    }


    @GetMapping("/welcome")
    public StringBuffer getAlbums(Model model, @AuthenticationPrincipal OidcUser principal) {

        // Get ID Token Object
        OidcIdToken idTokenObject = principal.getIdToken();
        // Get ID Token Value
        String idToken = idTokenObject.getTokenValue();

        return userService.userInfo(idToken);
    }

    @GetMapping("/adduser/{email}/{username}")
    public String addUser(@PathVariable("email") String email, @PathVariable("username") String userName) {
        return userService.addUser(email, userName);
    }

    @GetMapping("/getemail/criteria/query/{email}")
    public List<UserInfo> getEmail(@PathVariable("email") String email) {
        return userRepo.getUserEmail(email);
    }

    @GetMapping("/update/{old}/{new}")
    public void update(@PathVariable("old") String old, @PathVariable("new") String newEmail) {
        userService.update(old, newEmail);
    }

    @GetMapping("/groupby")
    public List<Long> grpby()
    {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<UserInfo> infoRoot = criteriaQuery.from(UserInfo.class);
//        criteriaQuery.select(criteriaBuilder.sum(infoRoot.get("id")));
//        criteriaQuery.where(criteriaBuilder.sum(infoRoot.get("id")));
       // Date date = new Date(stamp.getTime());
        DateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat f1 = new SimpleDateFormat("yyyy/MM/dd");
//        criteriaBuilder.function(f.format(new Date(String.valueOf(infoRoot.get("time")))),UserInfo.class);
        Expression<Long> timestamp= infoRoot.get("time");
        criteriaQuery.groupBy(infoRoot.get("time"));

//        criteriaQuery.multiselect(
//                criteriaBuilder.function("TO_CHAR",
//                        String.class,infoRoot.get("time"),
//                        criteriaBuilder.literal("yyyy-MM-dd")));


        criteriaQuery.where(criteriaBuilder.equal(criteriaBuilder.function("date", Date.class, infoRoot.get("time")), "12/07/2022"));

        TypedQuery<Long> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }

    @GetMapping("/grp")
    public List<UserInfo> grp()
    {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserInfo> criteriaQuery = cb.createQuery(UserInfo.class);
        Root<UserInfo> infoRoot = criteriaQuery.from(UserInfo.class);
//        criteriaQuery.select(infoRoot.get("time").as(java.sql.Date.class));
//        Expression<String> day = cb.literal("yyyy-MM-dd");
//
//        List<Predicate> predicates=new ArrayList<>();
//        predicates.add(cb.equal(cb.function("TRUNC", Date.class, infoRoot.get("time")), cb.function("TO_DATE", String.class, cb.parameter(String.class, "time"), cb.literal("YYYY-MM-DD"))));
//

//        criteriaQuery.select((Selection<? extends UserInfo>) predicates);
        criteriaQuery.groupBy(infoRoot.get("time"));
        TypedQuery<UserInfo> result=entityManager.createQuery(criteriaQuery);
        return result.getResultList();
    }

//    @GetMapping("/date")
//    public List<UserInfo> date() throws ParseException {
//        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//        CriteriaQuery<UserInfo> criteriaQuery = criteriaBuilder.createQuery(UserInfo.class);
//        Root<UserInfo> root = criteriaQuery.from(entityManager.getMetamodel().entity(UserInfo.class));
//
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
//        java.util.Date startDate = dateFormat.parse("24-02-2016");
//        java.util.Date endDate = dateFormat.parse("24-03-2016");
//
//        ParameterExpression<java.util.Date> parameter = criteriaBuilder.parameter(java.util.Date.class);
//
//        Predicate startDatePredicate = criteriaBuilder.greaterThanOrEqualTo(root.get(Discount_.discountStartDate).as(java.sql.Date.class), parameter);
//        Predicate endDatePredicate = criteriaBuilder.lessThanOrEqualTo(root.get(Discount_.discountEndDate).as(java.sql.Date.class), parameter);
//
//        Predicate startDateOrPredicate = criteriaBuilder.or(startDatePredicate, root.get(Discount_.discountStartDate).isNull());
//        Predicate endDateOrPredicate = criteriaBuilder.or(endDatePredicate, root.get(Discount_.discountEndDate).isNull());
//
//        Predicate and = criteriaBuilder.and(startDateOrPredicate, endDateOrPredicate);
//        criteriaQuery.where(and);
//
//        List<Discount> list = entityManager.createQuery(criteriaQuery)
//                .setParameter(parameter, startDate, TemporalType.DATE)
//                .setParameter(parameter, endDate, TemporalType.DATE)
//                .getResultList();
//    }



    @GetMapping("/timetodate")
    public List<UserInfo> timeToDate()
    {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserInfo> criteriaQuery = criteriaBuilder.createQuery(UserInfo.class);
        Root<UserInfo> from = criteriaQuery.from(UserInfo.class);
//
//        criteriaQuery.multiselect(
//                from.get("time").as(java.sql.Date.class),
//                criteriaBuilder.count(from)
//        );

        /*Add order by and group by clause*/
//        criteriaQuery.orderBy(criteriaBuilder.desc(from.get("time").as(java.sql.Date.class)));
//        criteriaQuery.groupBy(from.get("time"));
//        Expression<Number> dateAndTime=criteriaBuilder.quot(from.get("time"),1000);
//
//        Root<UserInfo> root = criteriaQuery.from(entityManager.getMetamodel().entity(UserInfo.class));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");


        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(ZoneOffset.UTC));


        criteriaQuery.groupBy(from.<java.sql.Date>get("time"));
        ParameterExpression<Calendar> parameter = criteriaBuilder.parameter(Calendar.class);
//        criteriaQuery.where(criteriaBuilder.greaterThanOrEqualTo(root.get(Discount_.discountStartDate).as(Calendar.class), parameter));
//        List<Discount> list = entityManager.createQuery(criteriaQuery).setParameter(parameter, calendar, TemporalType.DATE).getResultList();
        TypedQuery<UserInfo> query = entityManager.createQuery(criteriaQuery);
        List<UserInfo> results = query.getResultList();
        return results;
    }


    @GetMapping("/math")
    public List<Number> math()
    {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Number> criteriaQuery = criteriaBuilder.createQuery(Number.class);
        Subquery<Long> subcq = criteriaQuery.subquery(Long.class);
        Root from = criteriaQuery.from(UserInfo.class);


        subcq.select(from.get("time"));
        subcq.groupBy(from.get("time"));
        criteriaQuery.select(criteriaBuilder.count(subcq));

        Expression<Long> dateAndTime=criteriaBuilder.quot(from.get("time"),1000);
        Expression<Long> mul= criteriaBuilder.quot(criteriaBuilder.quot(from.get("time"),1000),86400000);
        Expression<Number> diff=criteriaBuilder.diff(criteriaBuilder.quot(from.get("time"),1000),mul);
        criteriaQuery.select(diff);
        subcq.select(from.get("time"));
//
//        subcq.groupBy(from.get("id"));
//        criteriaQuery.select(criteriaBuilder.count(subcq));
        TypedQuery<Number> query = entityManager.createQuery(criteriaQuery);
        List<Number> results = query.getResultList();
        System.out.println(results);
        return  results;
    }
}
