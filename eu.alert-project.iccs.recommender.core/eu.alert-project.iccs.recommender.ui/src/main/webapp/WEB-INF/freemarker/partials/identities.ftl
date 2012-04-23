<#import "/spring.ftl" as spring/>
<div id="identities-container">
<ul class="identities-list">
<#list identities as identity>
    <li>
        <table class="identity">
            <thead>
                <tr>
                    <th> <img src="<@spring.url "/static/images/user.png"/>" alt="User" border="none"/></th>
                    <th class="uuidvalue">${identity.uuid}</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td colspan="2">${identity.name}</td>
                    <#--<td  colspan="2" class="realidentity"><span class="dropt">${identity.name} ${identity.lastname}-->
                    <#--<span style="width:500px;">names currently hidden for privacy purposes</span></span>-->
                    <#--</td>-->
                </tr>
                <!--                For each row also display the similarity and the ranking-->
                <tr>
                    <td> <img src="<@spring.url "/static/images/similarity.png"/>" alt="similarity" border="none"/></td>
                    <td> Similarity: ${identity.similarity} </td>
                </tr>
                <tr>
                    <td> <img src="<@spring.url "/static/images/ranking.png"/>" alt="ranking" border="none"/></td>
                    <td> Competency:${identity.ranking}</td>
                </tr>
            </tbody>
        </table>
    </li>
</#list>
</ul>
</div>


