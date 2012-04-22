<div id="bugs-container">
<ul class="bugs-list">
<#list developer as bug>
    <li>
        <table class="bug">
            <thead>
                <tr>
                    <th>${bug.id?c}</th>
                </tr>
            </thead>
            <tbody>
                <#list bug.annotationMap?keys as key>
                <tr>
                    <td class="annot">${key}</td> <td class="annotweight">${bug.annotationMap[key]}</td>
                </tr>
                </#list>

            </tbody>
        </table>
    </li>
</#list>
</ul>
</div>


