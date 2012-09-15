<#macro HOOK hookName params="">
	<#if hooks[hookName]??>
		<#list hooks[hookName] as hook>
			<#if params??>
				${hook(params)}
			<#else>
				${hook()}
			</#if>
		</#list>
	</#if>
</#macro>