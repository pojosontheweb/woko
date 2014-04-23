<?xml version='1.0' encoding='utf-8'?>

<!-- XHTML-to-XHTML converter by Fletcher Penney
	specifically designed for use with MultiMarkdown created XHTML
	
	Adds a Table of Contents to the top of the XHTML document,
	and adds linkbacks from h1 and h2's.
	
	Also, an example of the sorts of things that can be done to customize
	the XHTML output of MultiMarkdown.
	
	This version starts ToC with h2 entries (useful with base header level = 2)
	
	MultiMarkdown Version 2.0.b6
	
	$Id: xhtml-toc-h2.xslt 499 2008-03-23 13:03:19Z fletcher $
-->

<!-- 
# Copyright (C) 2007-2008  Fletcher T. Penney <fletcher@fletcherpenney.net>
#
# This program is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation; either version 2 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program; if not, write to the
#    Free Software Foundation, Inc.
#    59 Temple Place, Suite 330
#    Boston, MA 02111-1307 USA
-->

	
<xsl:stylesheet
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:m="http://www.w3.org/1998/Math/MathML"
	exclude-result-prefixes="xsl"
	version="1.0">


	<xsl:variable name="newline">
<xsl:text>
</xsl:text>
	</xsl:variable>
	
	<xsl:output method='xml' version="1.0" encoding='utf-8' doctype-public="-//W3C//DTD XHTML 1.1 plus MathML 2.0//EN" doctype-system="http://www.w3.org/TR/MathML2/dtd/xhtml-math11-f.dtd" indent="no"/>

	<!-- the identity template, based on http://www.xmlplease.com/xhtmlxhtml -->
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="body">
		<xsl:copy>
			<xsl:value-of select="$newline"/>
					
			<!-- main content -->
			<div class="container">
																	  
				<div class="row">
					<div class="col-xs-12 col-sm-12 col-md-9 col-lg-9" role="main" id="affix">
						<xsl:apply-templates select="@*|node()"/>
					</div>
					<div class="col-md-3 col-lg-3 hidden-sm hidden-xs">
						<div data-spy="affix" data-offset-top="60" data-offset-bottom="200" role="complementary" id="right-nav">
							<ul class="nav">
								<xsl:apply-templates select="h1" mode="ToC"/>
								<xsl:value-of select="$newline"/>
							</ul>					
						</div>
					</div>
				</div>
				<footer class="row">
					<div class="col-sm-12">
						<div class="copyright pull-left">
							(c) The Woko Framework. POJOs on the Web ! 
						</div>
						<div class="pull-right">
							<a href="http://pojosontheweb.com">							 
								<img src="woko-logo-small.png" alt="woko logo"/>
							</a>
						</div>
					</div>
				</footer>
			</div>
		</xsl:copy>
		
		<script type="text/javascript">
			$(function() {
				$('body').scrollspy({ target: '#right-nav' });
			});
		</script>
	</xsl:template>	
	
	<!-- create ToC entry -->
	<xsl:template match="h1" mode="ToC">
		<xsl:value-of select="$newline"/>
		<xsl:variable name="link">
			<xsl:value-of select="@id"/>
		</xsl:variable>
		<xsl:variable name="myId">
			<xsl:value-of select="generate-id(.)"/>
		</xsl:variable>
		<li>
			<a id="ToC-{$link}" href="#{$link}">
				<xsl:apply-templates select="node()"/>
			</a>
			<xsl:if test="following::h2[1][preceding::h1[1]]">
				<xsl:value-of select="$newline"/>
				<ul class="nav subsections">
					<xsl:apply-templates select="following::h2[preceding::h1[1][generate-id() = $myId]]" mode="ToC"/>
					<xsl:value-of select="$newline"/>
				</ul>
				<xsl:value-of select="$newline"/>
			</xsl:if>
		</li>
	</xsl:template>

	<xsl:template match="h2" mode="ToC">
		<xsl:value-of select="$newline"/>
		<xsl:variable name="link">
			<xsl:value-of select="@id"/>
		</xsl:variable>
		<xsl:variable name="myId">
			<xsl:value-of select="generate-id(.)"/>
		</xsl:variable>
		<li>
			<a id="ToC-{$link}" href="#{$link}">
				<xsl:apply-templates select="node()"/>
			</a>
			<xsl:if test="following::h3[1][preceding::h2[1]]">
				<xsl:value-of select="$newline"/>
				<ul>
					<xsl:apply-templates select="following::h3[preceding::h2[1][generate-id() = $myId]]" mode="ToC"/>
					<xsl:value-of select="$newline"/>
				</ul>
				<xsl:value-of select="$newline"/>
			</xsl:if>
		</li>
	</xsl:template>

	<xsl:template match="h3" mode="ToC">
		<xsl:value-of select="$newline"/>
		<xsl:variable name="link">
			<xsl:value-of select="@id"/>
		</xsl:variable>
		<xsl:variable name="myId">
			<xsl:value-of select="generate-id(.)"/>
		</xsl:variable>
		<li>
			<a id="ToC-{$link}" href="#{$link}">
				<xsl:apply-templates select="node()"/>
			</a>
			<xsl:if test="following::h4[1][preceding::h3[1]]">
				<xsl:value-of select="$newline"/>
				<ul>
					<xsl:apply-templates select="following::h4[preceding::h3[1][generate-id() = $myId]]" mode="ToC"/>
					<xsl:value-of select="$newline"/>
				</ul>
				<xsl:value-of select="$newline"/>
			</xsl:if>
		</li>
	</xsl:template>

	<xsl:template match="h4" mode="ToC">
		<xsl:value-of select="$newline"/>
		<xsl:variable name="link">
			<xsl:value-of select="@id"/>
		</xsl:variable>
		<li>
			<a id="ToC-{$link}" href="#{$link}">
				<xsl:apply-templates select="node()"/>
			</a>
		</li>
	</xsl:template>
	
	<xsl:template match="h1">
		<xsl:variable name="link">
			<xsl:value-of select="@id"/>
		</xsl:variable>
		<h1 id="{$link}">
			<xsl:apply-templates select="@*|node()"/>
		</h1>
	</xsl:template>

	<xsl:template match="h2">
		<xsl:variable name="link">
			<xsl:value-of select="@id"/>
		</xsl:variable>
		<h2 id="{$link}">
			<xsl:apply-templates select="@*|node()"/>
		</h2>
	</xsl:template>

	<xsl:template match="h3">
		<xsl:variable name="link">
			<xsl:value-of select="@id"/>
		</xsl:variable>
		<h3 id="{$link}">
			<xsl:apply-templates select="@*|node()"/>
		</h3>
	</xsl:template>

	<xsl:template match="h4">
		<xsl:variable name="link">
			<xsl:value-of select="@id"/>
		</xsl:variable>
		<h4 id="{$link}">
			<xsl:apply-templates select="@*|node()"/>
		</h4>
	</xsl:template>
	
</xsl:stylesheet>