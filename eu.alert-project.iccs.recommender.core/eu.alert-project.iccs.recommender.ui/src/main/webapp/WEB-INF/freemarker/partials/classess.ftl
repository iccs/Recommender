<div id="classes-container">

<h1>Classes</h1>
<ul class="classes-list">
<#list classess as clazz>
    <li>
        <label for="check-${clazz}">${clazz}</label>
        <input
            type="checkbox"
            id="check-${clazz}"
            name="${clazz}"
            value="${clazz}"
            onclick="toggleSelected('${clazz}');"
            />
    </li>
</#list>
</ul>
</div>


