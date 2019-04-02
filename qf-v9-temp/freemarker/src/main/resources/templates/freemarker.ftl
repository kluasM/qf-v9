<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
    hello,${name}
    ID:<span>${product.id}</span>
    name:<span>${product.name}</span>
    //Use ?date, ?time, or ?datetime to tell FreeMarker the exact type.
    birthday:<span>${product.birthday?date}</span>
    birthday:<span>${product.birthday?time}</span>
    birthday:<span>${product.birthday?datetime}</span>

    <table>
        <tr>
            <td>ID</td>
            <td>NAME</td>
            <td>BIRTHDAY</td>
        </tr>
        <#list list as p>
            <tr>
                <td>${p.id}</td>
                <td>${p.name}</td>
                <td>${p.birthday?date}</td>
            </tr>
        </#list>
    </table>

    <#if (age > 40)>
        大叔级
        <#elseif (age>30) >
          老腊肉
        <#else >
          小鲜肉
    </#if>

    ${msg!""}
    ${msg!"没有错误消息提示"}
</body>
</html>