<div id="identities-container">
<ul class="identities-list">
<#list identities as identity>
    <li>
        <table class="identity">
            <thead>
                <tr>
                    <th>${identity.uuid}</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td >${identity.name} ${identity.lastname}</td>
                </tr>
<!--                For each row also display the similarity and the ranking-->
                <tr>
                    <td >${identity.similarity} /  ${identity.ranking}</td>
                </tr>
            </tbody>
        </table>
    </li>
</#list>
</ul>
</div>


