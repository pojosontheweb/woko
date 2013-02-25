<%@ page import="woko.facets.builtin.bootstrap.all.SwitchTheme" %>
<%@ page import="woko.facets.builtin.bootstrap.all.Theme" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>

<%
    String themeName;
    Theme theme = (Theme)request.getSession().getAttribute(SwitchTheme.WOKO_THEME);
    if (theme == null)
        themeName = "bootstrap-V2.3.0".toLowerCase();
    else
        themeName = theme.name().toLowerCase();
%>

<!-- Masthead
================================================== -->
<header>
    <h1 class="page-header">
        Preview for the skin : <span style="text-transform: uppercase;"><%=themeName%></span>
    </h1>
    <p>
        As woko is coming with the Bootstrap CSS framework, we have integrated the themes coming from <a href="http://bootswatch.com">Bootswatch</a>
        directly out of the box.
    </p>
    <h3>How to use this theme permanently</h3>
    <p>
        Open the facet : <code>your.facets.package.MyLayout</code> and replace the the <code>getCssIncludes()</code> method with something like :
    </p>
    <pre>
    @Override
    List<String> getCssIncludes() {
        return [
            "/css/<%=themeName%>/bootstrap.css",
            "/css/woko.css",
            "/css/responsive.css"
        ]
    }
    </pre>
</header>

<!-- Typography
================================================== -->
<section id="typography">
    <div class="page-header">
        <h1>Typography</h1>
    </div>

    <!-- Headings & Paragraph Copy -->
    <div class="row">

        <div class="span4">
            <div class="well">
                <h1>h1. Heading 1</h1>
                <h2>h2. Heading 2</h2>
                <h3>h3. Heading 3</h3>
                <h4>h4. Heading 4</h4>
                <h5>h5. Heading 5</h5>
                <h6>h6. Heading 6</h6>
            </div>
        </div>

        <div class="span4">
            <h3>Example body text</h3>
            <p>Nullam quis risus eget urna mollis ornare vel eu leo. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Nullam id dolor id nibh ultricies vehicula ut id elit.</p>
            <p>Vivamus sagittis lacus vel augue laoreet rutrum faucibus dolor auctor. Duis mollis, est non commodo luctus, nisi erat porttitor ligula, eget lacinia odio sem nec elit. Donec sed odio dui.</p>
        </div>

        <div class="span4">
            <h3>Example addresses</h3>
            <address>
                <strong>Twitter, Inc.</strong><br>
                795 Folsom Ave, Suite 600<br>
                San Francisco, CA 94107<br>
                <abbr title="Phone">P:</abbr> (123) 456-7890
            </address>
            <address>
                <strong>Full Name</strong><br>
                <a href="mailto:#">first.last@gmail.com</a>
            </address>
        </div>

    </div>

    <div class="row">
        <div class="span6">
            <blockquote>
                <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer posuere erat a ante.</p>
                <small>Someone famous in <cite title="Source Title">Source Title</cite></small>
            </blockquote>
        </div>
        <div class="span6">
            <blockquote class="pull-right">
                <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer posuere erat a ante.</p>
                <small>Someone famous in <cite title="Source Title">Source Title</cite></small>
            </blockquote>
        </div>
    </div>

</section>


<!-- Navbar
================================================== -->
<section id="navbar">
    <div class="page-header">
        <h1>Navbars</h1>
    </div>
    <div class="navbar">
        <div class="navbar-inner">
            <div style="width: auto;" class="container">
                <a data-target=".nav-collapse" data-toggle="collapse" class="btn btn-navbar">
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </a>
                <a href="#" class="brand">Project name</a>
                <div class="nav-collapse">
                    <ul class="nav">
                        <li class="active"><a href="#">Home</a></li>
                        <li><a href="#">Link</a></li>
                        <li><a href="#">Link</a></li>
                        <li><a href="#">Link</a></li>
                        <li class="dropdown">
                            <a data-toggle="dropdown" class="dropdown-toggle" href="#">Dropdown <b class="caret"></b></a>
                            <ul class="dropdown-menu">
                                <li><a href="#">Action</a></li>
                                <li><a href="#">Another action</a></li>
                                <li><a href="#">Something else here</a></li>
                                <li class="divider"></li>
                                <li><a href="#">Separated link</a></li>
                            </ul>
                        </li>
                    </ul>
                    <form action="" class="navbar-search pull-left">
                        <input type="text" placeholder="Search" class="search-query span2">
                    </form>
                    <ul class="nav pull-right">
                        <li><a href="#">Link</a></li>
                        <li class="divider-vertical"></li>
                        <li class="dropdown">
                            <a data-toggle="dropdown" class="dropdown-toggle" href="#">Dropdown <b class="caret"></b></a>
                            <ul class="dropdown-menu">
                                <li><a href="#">Action</a></li>
                                <li><a href="#">Another action</a></li>
                                <li><a href="#">Something else here</a></li>
                                <li class="divider"></li>
                                <li><a href="#">Separated link</a></li>
                            </ul>
                        </li>
                    </ul>
                </div><!-- /.nav-collapse -->
            </div>
        </div><!-- /navbar-inner -->
    </div><!-- /navbar -->

    <div class="navbar navbar-inverse">
        <div class="navbar-inner">
            <div style="width: auto;" class="container">
                <a data-target=".nav-collapse" data-toggle="collapse" class="btn btn-navbar">
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </a>
                <a href="#" class="brand">Project name</a>
                <div class="nav-collapse">
                    <ul class="nav">
                        <li class="active"><a href="#">Home</a></li>
                        <li><a href="#">Link</a></li>
                        <li><a href="#">Link</a></li>
                        <li><a href="#">Link</a></li>
                        <li class="dropdown">
                            <a data-toggle="dropdown" class="dropdown-toggle" href="#">Dropdown <b class="caret"></b></a>
                            <ul class="dropdown-menu">
                                <li><a href="#">Action</a></li>
                                <li><a href="#">Another action</a></li>
                                <li><a href="#">Something else here</a></li>
                                <li class="divider"></li>
                                <li><a href="#">Separated link</a></li>
                            </ul>
                        </li>
                    </ul>
                    <form action="" class="navbar-search pull-left">
                        <input type="text" placeholder="Search" class="search-query span2">
                    </form>
                    <ul class="nav pull-right">
                        <li><a href="#">Link</a></li>
                        <li class="divider-vertical"></li>
                        <li class="dropdown">
                            <a data-toggle="dropdown" class="dropdown-toggle" href="#">Dropdown <b class="caret"></b></a>
                            <ul class="dropdown-menu">
                                <li><a href="#">Action</a></li>
                                <li><a href="#">Another action</a></li>
                                <li><a href="#">Something else here</a></li>
                                <li class="divider"></li>
                                <li><a href="#">Separated link</a></li>
                            </ul>
                        </li>
                    </ul>
                </div><!-- /.nav-collapse -->
            </div>
        </div><!-- /navbar-inner -->
    </div><!-- /navbar -->

</section>



<!-- Buttons
================================================== -->
<section id="buttons">
    <div class="page-header">
        <h1>Buttons</h1>
    </div>
    <table class="table table-bordered table-striped">
        <thead>
        <tr>
            <th>Button</th>
            <th>Large Button</th>
            <th>Small Button</th>
            <th>Disabled Button</th>
            <th>Button with Icon</th>
            <th>Split Button</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td><a href="#" class="btn">Default</a></td>
            <td><a href="#" class="btn btn-large">Default</a></td>
            <td><a href="#" class="btn btn-small">Default</a></td>
            <td><a href="#" class="btn disabled">Default</a></td>
            <td><a href="#" class="btn"><i class="icon-cog"></i> Default</a></td>
            <td>
                <div class="btn-group">
                    <a href="#" class="btn">Default</a>
                    <a href="#" data-toggle="dropdown" class="btn dropdown-toggle"><span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li><a href="#">Action</a></li>
                        <li><a href="#">Another action</a></li>
                        <li><a href="#">Something else here</a></li>
                        <li class="divider"></li>
                        <li><a href="#">Separated link</a></li>
                    </ul>
                </div><!-- /btn-group -->
            </td>
        </tr>
        <tr>
            <td><a href="#" class="btn btn-primary">Primary</a></td>
            <td><a href="#" class="btn btn-primary btn-large">Primary</a></td>
            <td><a href="#" class="btn btn-primary btn-small">Primary</a></td>
            <td><a href="#" class="btn btn-primary disabled">Primary</a></td>
            <td><a href="#" class="btn btn-primary"><i class="icon-shopping-cart icon-white"></i> Primary</a></td>
            <td>
                <div class="btn-group">
                    <a href="#" class="btn btn-primary">Primary</a>
                    <a href="#" data-toggle="dropdown" class="btn btn-primary dropdown-toggle"><span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li><a href="#">Action</a></li>
                        <li><a href="#">Another action</a></li>
                        <li><a href="#">Something else here</a></li>
                        <li class="divider"></li>
                        <li><a href="#">Separated link</a></li>
                    </ul>
                </div><!-- /btn-group -->
            </td>
        </tr>
        <tr>
            <td><a href="#" class="btn btn-info">Info</a></td>
            <td><a href="#" class="btn btn-info btn-large">Info</a></td>
            <td><a href="#" class="btn btn-info btn-small">Info</a></td>
            <td><a href="#" class="btn btn-info disabled">Info</a></td>
            <td><a href="#" class="btn btn-info"><i class="icon-exclamation-sign icon-white"></i> Info</a></td>
            <td>
                <div class="btn-group">
                    <a href="#" class="btn btn-info">Info</a>
                    <a href="#" data-toggle="dropdown" class="btn btn-info dropdown-toggle"><span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li><a href="#">Action</a></li>
                        <li><a href="#">Another action</a></li>
                        <li><a href="#">Something else here</a></li>
                        <li class="divider"></li>
                        <li><a href="#">Separated link</a></li>
                    </ul>
                </div><!-- /btn-group -->
            </td>
        </tr>
        <tr>
            <td><a href="#" class="btn btn-success">Success</a></td>
            <td><a href="#" class="btn btn-success btn-large">Success</a></td>
            <td><a href="#" class="btn btn-success btn-small">Success</a></td>
            <td><a href="#" class="btn btn-success disabled">Success</a></td>
            <td><a href="#" class="btn btn-success"><i class="icon-ok icon-white"></i> Success</a></td>
            <td>
                <div class="btn-group">
                    <a href="#" class="btn btn-success">Success</a>
                    <a href="#" data-toggle="dropdown" class="btn btn-success dropdown-toggle"><span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li><a href="#">Action</a></li>
                        <li><a href="#">Another action</a></li>
                        <li><a href="#">Something else here</a></li>
                        <li class="divider"></li>
                        <li><a href="#">Separated link</a></li>
                    </ul>
                </div><!-- /btn-group -->
            </td>
        </tr>
        <tr>
            <td><a href="#" class="btn btn-warning">Warning</a></td>
            <td><a href="#" class="btn btn-warning btn-large">Warning</a></td>
            <td><a href="#" class="btn btn-warning btn-small">Warning</a></td>
            <td><a href="#" class="btn btn-warning disabled">Warning</a></td>
            <td><a href="#" class="btn btn-warning"><i class="icon-warning-sign icon-white"></i> Warning</a></td>
            <td>
                <div class="btn-group">
                    <a href="#" class="btn btn-warning">Warning</a>
                    <a href="#" data-toggle="dropdown" class="btn btn-warning dropdown-toggle"><span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li><a href="#">Action</a></li>
                        <li><a href="#">Another action</a></li>
                        <li><a href="#">Something else here</a></li>
                        <li class="divider"></li>
                        <li><a href="#">Separated link</a></li>
                    </ul>
                </div><!-- /btn-group -->
            </td>
        </tr>
        <tr>
            <td><a href="#" class="btn btn-danger">Danger</a></td>
            <td><a href="#" class="btn btn-danger btn-large">Danger</a></td>
            <td><a href="#" class="btn btn-danger btn-small">Danger</a></td>
            <td><a href="#" class="btn btn-danger disabled">Danger</a></td>
            <td><a href="#" class="btn btn-danger"><i class="icon-remove icon-white"></i> Danger</a></td>
            <td>
                <div class="btn-group">
                    <a href="#" class="btn btn-danger">Danger</a>
                    <a href="#" data-toggle="dropdown" class="btn btn-danger dropdown-toggle"><span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li><a href="#">Action</a></li>
                        <li><a href="#">Another action</a></li>
                        <li><a href="#">Something else here</a></li>
                        <li class="divider"></li>
                        <li><a href="#">Separated link</a></li>
                    </ul>
                </div><!-- /btn-group -->
            </td>
        </tr>
        <tr>
            <td><a href="#" class="btn btn-inverse">Inverse</a></td>
            <td><a href="#" class="btn btn-inverse btn-large">Inverse</a></td>
            <td><a href="#" class="btn btn-inverse btn-small">Inverse</a></td>
            <td><a href="#" class="btn btn-inverse disabled">Inverse</a></td>
            <td><a href="#" class="btn btn-inverse"><i class="icon-random icon-white"></i> Inverse</a></td>
            <td>
                <div class="btn-group">
                    <a href="#" class="btn btn-inverse">Inverse</a>
                    <a href="#" data-toggle="dropdown" class="btn btn-inverse dropdown-toggle"><span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li><a href="#">Action</a></li>
                        <li><a href="#">Another action</a></li>
                        <li><a href="#">Something else here</a></li>
                        <li class="divider"></li>
                        <li><a href="#">Separated link</a></li>
                    </ul>
                </div><!-- /btn-group -->
            </td>
        </tr>
        </tbody>
    </table>

</section>


<!-- Forms
================================================== -->
<section id="forms">
    <div class="page-header">
        <h1>Forms</h1>
    </div>

    <div class="row">
        <div class="span10 offset1">

            <form class="well form-search">
                <input type="text" class="input-medium search-query">
                <button class="btn" type="submit">Search</button>
            </form>

            <form class="well form-search">
                <input type="text" placeholder="Email" class="input-small">
                <input type="password" placeholder="Password" class="input-small">
                <button class="btn" type="submit">Go</button>
            </form>


            <form class="form-horizontal well">
                <fieldset>
                    <legend>Controls Bootstrap supports</legend>
                    <div class="control-group">
                        <label for="input01" class="control-label">Text input</label>
                        <div class="controls">
                            <input type="text" id="input01" class="input-xlarge">
                            <p class="help-block">In addition to freeform text, any HTML5 text-based input appears like so.</p>
                        </div>
                    </div>
                    <div class="control-group">
                        <label for="optionsCheckbox" class="control-label">Checkbox</label>
                        <div class="controls">
                            <label class="checkbox">
                                <input type="checkbox" value="option1" id="optionsCheckbox">
                                Option one is this and that&mdash;be sure to include why it's great
                            </label>
                        </div>
                    </div>
                    <div class="control-group">
                        <label for="select01" class="control-label">Select list</label>
                        <div class="controls">
                            <select id="select01">
                                <option>something</option>
                                <option>2</option>
                                <option>3</option>
                                <option>4</option>
                                <option>5</option>
                            </select>
                        </div>
                    </div>
                    <div class="control-group">
                        <label for="multiSelect" class="control-label">Multicon-select</label>
                        <div class="controls">
                            <select id="multiSelect" multiple="multiple">
                                <option>1</option>
                                <option>2</option>
                                <option>3</option>
                                <option>4</option>
                                <option>5</option>
                            </select>
                        </div>
                    </div>
                    <div class="control-group">
                        <label for="fileInput" class="control-label">File input</label>
                        <div class="controls">
                            <input type="file" id="fileInput" class="input-file">
                        </div>
                    </div>
                    <div class="control-group">
                        <label for="textarea" class="control-label">Textarea</label>
                        <div class="controls">
                            <textarea rows="3" id="textarea" class="input-xlarge"></textarea>
                        </div>
                    </div>
                    <div class="control-group">
                        <label for="focusedInput" class="control-label">Focused input</label>
                        <div class="controls">
                            <input type="text" value="This is focused…" id="focusedInput" class="input-xlarge focused">
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label">Uneditable input</label>
                        <div class="controls">
                            <span class="input-xlarge uneditable-input">Some value here</span>
                        </div>
                    </div>
                    <div class="control-group">
                        <label for="disabledInput" class="control-label">Disabled input</label>
                        <div class="controls">
                            <input type="text" disabled="" placeholder="Disabled input here…" id="disabledInput" class="input-xlarge disabled">
                        </div>
                    </div>
                    <div class="control-group">
                        <label for="optionsCheckbox2" class="control-label">Disabled checkbox</label>
                        <div class="controls">
                            <label class="checkbox">
                                <input type="checkbox" disabled="" value="option1" id="optionsCheckbox2">
                                This is a disabled checkbox
                            </label>
                        </div>
                    </div>
                    <div class="control-group warning">
                        <label for="inputWarning" class="control-label">Input with warning</label>
                        <div class="controls">
                            <input type="text" id="inputWarning">
                            <span class="help-inline">Something may have gone wrong</span>
                        </div>
                    </div>
                    <div class="control-group error">
                        <label for="inputError" class="control-label">Input with error</label>
                        <div class="controls">
                            <input type="text" id="inputError">
                            <span class="help-inline">Please correct the error</span>
                        </div>
                    </div>
                    <div class="control-group success">
                        <label for="inputSuccess" class="control-label">Input with success</label>
                        <div class="controls">
                            <input type="text" id="inputSuccess">
                            <span class="help-inline">Woohoo!</span>
                        </div>
                    </div>
                    <div class="control-group success">
                        <label for="selectError" class="control-label">Select with success</label>
                        <div class="controls">
                            <select id="selectError">
                                <option>1</option>
                                <option>2</option>
                                <option>3</option>
                                <option>4</option>
                                <option>5</option>
                            </select>
                            <span class="help-inline">Woohoo!</span>
                        </div>
                    </div>
                    <div class="form-actions">
                        <button class="btn btn-primary" type="submit">Save changes</button>
                        <button class="btn" type="reset">Cancel</button>
                    </div>
                </fieldset>
            </form>
        </div>
    </div>

</section>

<!-- Tables
================================================== -->
<section id="tables">
    <div class="page-header">
        <h1>Tables</h1>
    </div>

    <table class="table table-bordered table-striped table-hover">
        <thead>
        <tr>
            <th>#</th>
            <th>First Name</th>
            <th>Last Name</th>
            <th>Username</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td>1</td>
            <td>Mark</td>
            <td>Otto</td>
            <td>@mdo</td>
        </tr>
        <tr>
            <td>2</td>
            <td>Jacob</td>
            <td>Thornton</td>
            <td>@fat</td>
        </tr>
        <tr>
            <td>3</td>
            <td>Larry</td>
            <td>the Bird</td>
            <td>@twitter</td>
        </tr>
        </tbody>
    </table>
</section>


<!-- Miscellaneous
================================================== -->
<section id="miscellaneous">
<div class="page-header">
    <h1>Miscellaneous</h1>
</div>

<div class="row">
    <div class="span4">

        <h3 id="breadcrumbs">Breadcrumbs</h3>
        <ul class="breadcrumb">
            <li class="active">Home</li>
        </ul>
        <ul class="breadcrumb">
            <li><a href="#">Home</a> <span class="divider">/</span></li>
            <li><a href="#">Library</a> <span class="divider">/</span></li>
            <li class="active">Data</li>
        </ul>
    </div>
    <div class="span4">
        <h3 id="pagination">Pagination</h3>
        <div class="pagination">
            <ul>
                <li><a href="#">←</a></li>
                <li class="active"><a href="#">10</a></li>
                <li class="disabled"><a href="#">...</a></li>
                <li><a href="#">20</a></li>
                <li><a href="#">→</a></li>
            </ul>
        </div>
        <div class="pagination pagination-centered">
            <ul>
                <li class="active"><a href="#">1</a></li>
                <li><a href="#">2</a></li>
                <li><a href="#">3</a></li>
                <li><a href="#">4</a></li>
                <li><a href="#">5</a></li>
            </ul>
        </div>
    </div>

    <div class="span4">
        <h3 id="pager">Pagers</h3>

        <ul class="pager">
            <li><a href="#">Previous</a></li>
            <li><a href="#">Next</a></li>
        </ul>

        <ul class="pager">
            <li class="previous disabled"><a href="#">← Older</a></li>
            <li class="next"><a href="#">Newer →</a></li>
        </ul>
    </div>
</div>


<!-- Navs
================================================== -->

<div class="row">
    <div class="span4">

        <h3 id="tabs">Tabs</h3>
        <ul class="nav nav-tabs">
            <li class="active"><a data-toggle="tab" href="#A">Section 1</a></li>
            <li><a data-toggle="tab" href="#B">Section 2</a></li>
            <li><a data-toggle="tab" href="#C">Section 3</a></li>
        </ul>
        <div class="tabbable">
            <div class="tab-content">
                <div id="A" class="tab-pane active">
                    <p>I'm in Section A.</p>
                </div>
                <div id="B" class="tab-pane">
                    <p>Howdy, I'm in Section B.</p>
                </div>
                <div id="C" class="tab-pane">
                    <p>What up girl, this is Section C.</p>
                </div>
            </div>
        </div> <!-- /tabbable -->

    </div>
    <div class="span4">
        <h3 id="pills">Pills</h3>
        <ul class="nav nav-pills">
            <li class="active"><a href="#">Home</a></li>
            <li><a href="#">Profile</a></li>
            <li class="dropdown">
                <a href="#" data-toggle="dropdown" class="dropdown-toggle">Dropdown <b class="caret"></b></a>
                <ul class="dropdown-menu">
                    <li><a href="#">Action</a></li>
                    <li><a href="#">Another action</a></li>
                    <li><a href="#">Something else here</a></li>
                    <li class="divider"></li>
                    <li><a href="#">Separated link</a></li>
                </ul>
            </li>
            <li class="disabled"><a href="#">Disabled link</a></li>
        </ul>
    </div>

    <div class="span4">

        <h3 id="list">Lists</h3>

        <div style="padding: 8px 0;" class="well">
            <ul class="nav nav-list">
                <li class="nav-header">List header</li>
                <li class="active"><a href="#">Home</a></li>
                <li><a href="#">Library</a></li>
                <li><a href="#">Applications</a></li>
                <li class="nav-header">Another list header</li>
                <li><a href="#">Profile</a></li>
                <li><a href="#">Settings</a></li>
                <li class="divider"></li>
                <li><a href="#">Help</a></li>
            </ul>
        </div>
    </div>
</div>


<!-- Labels
================================================== -->

<div class="row">
    <div class="span6">
        <h3 id="labels">Labels</h3>
        <span class="label">Default</span>
        <span class="label label-success">Success</span>
        <span class="label label-warning">Warning</span>
        <span class="label label-important">Important</span>
        <span class="label label-info">Info</span>
        <span class="label label-inverse">Inverse</span>
    </div>
    <div class="span6">
        <h3 id="badges">Badges</h3>
        <span class="badge">Default</span>
        <span class="badge badge-success">Success</span>
        <span class="badge badge-warning">Warning</span>
        <span class="badge badge-important">Important</span>
        <span class="badge badge-info">Info</span>
        <span class="badge badge-inverse">Inverse</span>
    </div>
</div>
<br>

<!-- Progress bars
================================================== -->


<h3 id="progressbars">Progress bars</h3>

<div class="row">
    <div class="span4">
        <div class="progress">
            <div style="width: 60%;" class="bar"></div>
        </div>
    </div>
    <div class="span4">
        <div class="progress progress-info progress-striped">
            <div style="width: 20%;" class="bar"></div>
        </div>
    </div>
    <div class="span4">
        <div class="progress progress-danger progress-striped active">
            <div style="width: 45%" class="bar"></div>
        </div>
    </div>
</div>
<br>


<!-- Alerts & Messages
================================================== -->


<h3 id="alerts">Alerts</h3>

<div class="row">
    <div class="span12">
        <div class="alert alert-block">
            <a class="close">×</a>
            <h4 class="alert-heading">Alert block</h4>
            <p>Best check yo self, you're not looking too good. Nulla vitae elit libero, a pharetra augue. Praesent commodo cursus magna, vel scelerisque nisl consectetur et.</p>
        </div>
    </div>
</div>
<div class="row">
    <div class="span4">
        <div class="alert alert-error">
            <a class="close">×</a>
            <strong>Error</strong> Change a few things up and try submitting again.
        </div>
    </div>
    <div class="span4">
        <div class="alert alert-success">
            <a class="close">×</a>
            <strong>Success</strong> You successfully read this important alert message.
        </div>
    </div>
    <div class="span4">
        <div class="alert alert-info">
            <a class="close">×</a>
            <strong>Information</strong> This alert needs your attention, but it's not super important.
        </div>
    </div>
</div>


</section>

</div>
