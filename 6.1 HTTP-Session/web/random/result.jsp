<%--
  Created by IntelliJ IDEA.
  User: simon
  Date: 18.04.15
  Time: 22:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head lang="en">
  <meta charset="UTF-8">
  <title>OPWebApp - Get</title>

  <!-- Styles -->
  <link rel="stylesheet" href="random/css/normalize.css" media="screen,projection"/>
  <link rel="stylesheet" href="random/css/materialize.min.css">
  <link rel="stylesheet" href="random/css/main.css"/>

  <!--Let browser know website is optimized for mobile-->
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no"/>

</head>
<body class="indigo lighten-3">
  <div class="container valign-wrapper">

    <!-- Result-card -->
    <div id="error-card">
      <div class="col s12 m6">
        <div class="card black-text">
          <div class="card-content">
            <span class="card-title black-text">Ergebnis</span>
            <p>
              Zufallszahl: <%= request.getAttribute("randomNumber") %> </br>
              Session Aufrufe: <%= session.getAttribute("sessionCounter") %>
            </p>
          </div>
          <div class="card-action">
            <a href='/random/get.html'>Zurück zu get</a>
            <a href="/random/post.html">Zurück zu post</a>
          </div>
        </div>
      </div>
    </div>

<!-- Scripts -->
<script src="js/jquery.min.js"></script>
<script src="js/materialize.min.js"></script>

</body>
</html>
