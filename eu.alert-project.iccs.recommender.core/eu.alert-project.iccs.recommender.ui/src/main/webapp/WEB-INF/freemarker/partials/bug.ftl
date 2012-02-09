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
            <tr>
                <td>${bug.description}</td>
            </tr>
        </tbody>
    </table>
    <#else>
        <h1>No bug selected</h1>
    </#if>
</div>
