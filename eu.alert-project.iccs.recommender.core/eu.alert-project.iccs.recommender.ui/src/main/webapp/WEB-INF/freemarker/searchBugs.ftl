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
<body class="search">
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


    function searchBug(){

        var link = $("#search-dev-button");

        if(ICCS.isLocked(link)){
            return;
        }

        $("#devview").fadeOut(function(){
            $("#devview").html("");
        });

        $("#bugs").html("");

        lockLinks();

        $.get(
            "<@spring.url "/search/bug"/>/"+getSearchString(),
            function(data){

                if(data == undefined){
                    unlockLinks();
                    return;
                }

               $("#devview").html(data);
               $("#devview").fadeIn();

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
        ICCS.lockLink("#search-dev-button","");

    }

    function unlockLinks(){

        console.log("unlockLinks");

        $("#search-loader").fadeOut(function(){
            ICCS.unlockLink("#search-dev-button","Search Bug");

        });

    }

    function toggleSelected(id){

        console.log($("#bugs").has("#identities-"+id).size());


        if($("#bugs").has("#identities-"+id).size()> 0){

            console.log("Has identity id");
            $("#identities-"+id).fadeOut(function(){
                $("#identities-"+id).remove();
            });

        }else{

            console.log("Need to fetch the bugs");
            $.get(
                "<@spring.url "/search/identities"/>/"+id +"/"+getSearchString(),
                function(data){

                    $("#bugs").append('<div id="identities-'+id+'" style="display:none"></div>');

                    $("#identities-"+id).html(data);

                    $("#identities-"+id).fadeIn();
                }
            )
        }

    }

</script>
    <div class="container">
        <h1> Start by looking for a bug</h1>
        <div id="search-control">
            <input type="text" id="search-text">
            <a id="search-dev-button" class="search-button" href="#" onclick="searchBug();return false;">Search Bug</a>
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