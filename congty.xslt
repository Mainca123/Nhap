<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:msxsl="urn:schemas-microsoft-com:xslt" exclude-result-prefixes="msxsl"
>
    <xsl:output method="html"/>

    <xsl:template match="DS">
		<html>
			<style>
				table{
				border: 1px solid black;
				width: 100%;
				background-color: yellow;
				color: red;
				font-size: 24px;
				}
			</style>
			<body>
				<xsl:for-each select="congty">
					<h2>Tên công ty: <xsl:value-of select="@tencongty"/></h2>
					<xsl:for-each select="donvi">
						<h3>Tên đơn vị: <xsl:value-of select="tendv"/></h3>
						<table border="1" cellspacing="0" cellpadding="0">
							<tr>
								<th>STT</th>
								<th>Họ tên</th>
								<th>Ngay sinh</th>
								<th>Giới tính</th>
								<th>Lương</th>
							</tr>
							<xsl:for-each select="nhanvien">
								<tr>
									<td><xsl:value-of select="position()"/></td>
									<td><xsl:value-of select="ten"/></td>
									<td><xsl:value-of select="ngaysinh"/></td>
									<td><xsl:value-of select="giotinh"/></td>
									<xsl:variable name="socong" select="ngaycong"/>
									<xsl:variable name="luong">
										<xsl:choose>
											<xsl:when test="$socong &lt;= 20">
												<xsl:value-of select="$socong*1500"/>
											</xsl:when>
											<xsl:when test="$socong &lt;=25">
												<xsl:value-of select="$socong*1800"/>
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="$socong*2000"/>
											</xsl:otherwise>
											</xsl:choose>
									</xsl:variable>
									<td><xsl:value-of select="$luong"/></td>
								</tr>
							</xsl:for-each>
						</table>
					</xsl:for-each>
					<br/>
					<hr size="10px" style="background-color: black;"/>
					<br/>
				</xsl:for-each>
			</body>
		</html>
    </xsl:template>
</xsl:stylesheet>
