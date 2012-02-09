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
            </tbody>
        </table>
    </li>
</#list>
</ul>
</div>


