<!-- freemarker macros have to be imported into a namespace.  We strongly
recommend sticking to 'spring' -->
<#import "spring.ftl" as spring/>
<#import "macro/iccs.ftl" as iccs/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
    <title>Developer Recommendation for a given Bug</title>
    <link rel="stylesheet/less" type="text/css" href="<@spring.url "/static/css/main.less"/>">
    <script src="<@spring.url "/static/js/less-1.1.3.min.js" />" type="text/javascript"></script>
    <script src="<@spring.url "/static/js/stardom.js" />" type="text/javascript"></script>
    <script src="<@spring.url "/static/js/jquery-1.6.1-min.js"/>" type="text/javascript"></script>
    <script src="<@spring.url "/static/js/jquery.timers-1.2.js"/>" type="text/javascript"></script>
</head>
<body class="search">
    
    
<script type="text/javascript">

    $(document).ready(function(){
        $('#search-text').keyup('keypress', function(e) {
            if(e.keyCode==13){
                searchBug();
            }else if(e.keyCode==27){
                $(this).val("");
                $("#search-results").html("<h3>Type a bug id and press ENTER</h3>");
            }
        });

        $('#search-text').focus();
    });


    function searchBug(){

        var link = $("#search-bug-button");

        if(ICCS.isLocked(link)){
            return;
        }

        $("#bugview").fadeOut(function(){
            $("#bugview").html("");
        });

        $("#developers").html("");

        lockLinks();

        $.get(
            "<@spring.url "/search/bug"/>/"+getSearchString(),
            function(data){

                if(data == undefined){
                    unlockLinks();
                    return;
                }

               $("#bugview").html(data);
               $("#bugview").fadeIn();

               getUserClasses();

            }
        );

    }

    function getUserClasses(){

        $.get(
                "<@spring.url "/search/class"/>/"+getSearchString(),
                function(data){

                    if(data == undefined){
                        unlockLinks();
                        return;
                    }

                    $("#classes").html(data);

                    unlockLinks();

                }
        );

    }



    function getSearchString(){
        return $("#search-text").val();
    }

    function lockLinks(){

        $("#search-loader").fadeIn();
        ICCS.lockLink("#search-bug-button","");

    }

    function unlockLinks(){

        console.log("unlockLinks");

        $("#search-loader").fadeOut(function(){
            ICCS.unlockLink("#search-bug-button","Search Bug");

        });

    }

    function toggleSelected(id){

        console.log($("#developers").has("#identities-"+id).size());


        if($("#developers").has("#identities-"+id).size()> 0){

            console.log("Has identity id");
            $("#identities-"+id).fadeOut(function(){
                $("#identities-"+id).remove();
            });

        }else{

            console.log("Need to fetch the developers");
            $.get(
                "<@spring.url "/search/identities"/>/"+id +"/"+getSearchString(),
                function(data){

                    $("#developers").append('<div id="identities-'+id+'" style="display:none"></div>');

                    $("#identities-"+id).html(data);

                    $("#identities-"+id).fadeIn();
                }
            )
        }

    }

</script>

<div class="container">
    <div align=”center”>
    <img src="<@spring.url "/static/images/banner.png"/>" alt="Alert Banner" border="none"/>
</div>
        <h1> Start by looking for a bug</h1>
        <div id="search-control">
            <input type="text" id="search-text">
            <a id="search-bug-button" class="search-button" href="#" onclick="searchBug();return false;">Search Bug</a>
            <@iccs.loader id="search"/>
        </div>
        <div style="clear:both;"></div>
        <div id="results-container">

            <div id="bugview" style="display: none"></div>
            <div id="widgets">
                <div id="classes"></div>
                <div id="developers"></div>
            </div>
        </div>
    </div>
</body>
</html>