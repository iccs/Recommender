<div class="bugs">
    <#if bug??>
    <table>
        <thead>
            <tr>
                <th>#{bug.id}</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td>${bug.subject}</td>
            </tr>
            <#list bug.annotationMap?keys as key><tr>
                <td class="annot">${key}</td> <td class="annotweight">${bug.annotationMap[key]}</td>
            </tr>
            </#list>
        </tbody>
    </table>
    <#else>
        <h1>No bug selected</h1>
    </#if>
</div>
