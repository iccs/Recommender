<!-- freemarker macros have to be imported into a namespace.  We strongly
recommend sticking to 'spring' -->
<#import "spring.ftl" as spring/>
<#import "macro/iccs.ftl" as iccs/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
    <title>Bug Recommendation for a given Developer</title>
    <link rel="stylesheet/less" type="text/css" href="<@spring.url "/static/css/main.less"/>">
    <script src="<@spring.url "/static/js/less-1.1.3.min.js" />" type="text/javascript"></script>
    <script src="<@spring.url "/static/js/stardom.js" />" type="text/javascript"></script>
    <script src="<@spring.url "/static/js/jquery-1.6.1-min.js"/>" type="text/javascript"></script>
    <script src="<@spring.url "/static/js/jquery.timers-1.2.js"/>" type="text/javascript"></script>
</head>
<body class="developer">
<script type="text/javascript">

    $(document).ready(function(){
        $('#search-text').keyup('keypress', function(e) {
            if(e.keyCode==13){
                searchDev();
            }else if(e.keyCode==27){
                $(this).val("");
                $("#search-results").html("<h3>Type a Developer Uuid and press ENTER</h3>");
            }
        });

        $('#search-text').focus();
    });


    function searchDev(){
      
        var link = $("#search-dev-button");

        if(ICCS.isLocked(link)){
            return;
        }

        $("#devview").fadeOut(function(){
            $("#devview").html("");
        });

        lockLinks();
  $("#devview").html("trying to find it");
        $.get(
            "<@spring.url "/developer/developer/search"/>/"+getSearchString(),
            function(data){

                if(data == undefined){
                    unlockLinks();
                    return;
                }

               $("#devview").html(data);
               $("#devview").fadeIn();
               unlockLinks();
               

            }
        );

    }




    function getSearchString(){
        return $("#search-text").val();
    }

    function lockLinks(){

        $("#search-loader").fadeIn();
        ICCS.lockLink("#search-dev-button","");

    }


    

</script>
    <div class="container">
        <h1> Start by looking for a developer</h1>
        <div id="search-control">
            <input type="text" id="search-text">
            <a id="search-dev-button" class="search-button" href="#" onclick="searchDev();return false;">Search Developer</a>
            <@iccs.loader id="search"/>
        </div>
        <div style="clear:both;"></div>
        <div id="results-container">

            <div id="devview" style="display: none"></div>
            <div id="widgets">
                <div id="classes"></div>
                <div id="bugs"></div>
            </div>
        </div>
    </div>
</body>
</html>