package woko.actions

import net.sourceforge.stripes.validation.ValidationErrors
import net.sourceforge.stripes.action.ActionBean
import net.sourceforge.stripes.action.ActionBeanContext
import net.sourceforge.stripes.config.Configuration
import net.sourceforge.stripes.controller.DefaultActionBeanPropertyBinder

import woko.util.pexpressions.ExpressionTree
import woko.util.WLogger

class WokoMapPropertyBinder { 
//implements ActionBeanPropertyBinder {

  private static final WLogger logger = WLogger.getLogger(WokoMapPropertyBinder.class)

  private Configuration configuration
  private DefaultActionBeanPropertyBinder defaultBinder = new DefaultActionBeanPropertyBinder()

  void init(Configuration configuration) {
    this.configuration = configuration
    defaultBinder.init configuration
  }

  ValidationErrors bind(ActionBean bean, ActionBeanContext context, boolean validate) {
    logger.debug("Binding to bean $bean, running default binder...")
    // invoke the default binder for all regular Stripes binding
    ValidationErrors errs = defaultBinder.bind(bean, context, validate)

    logger.debug("Default binder returned validation errors : $errs. Doing the map binding now...")

    // now do the map binding !
    // first compute the expression tree for the parameters
    def params = context.request.getParameterMap()
    ExpressionTree et = new ExpressionTree()
    params.each { k,v ->
      def val
      if (v.length==0) {
        val = ''
      } else if (v.length==1) {
        val = v[0]
      } else {
        val = v
      }
      def expr = "actionBean.$k=$val"
      et.addToTree(expr)
    }

    logger.debug("Computed expression tree :\n$et")

    // bind
    def map = et.toMap()
    def props = map.get('actionBean')
    doBind(bean, props)

    logger.debug("Binding over for $bean, returning $errs")
    
    return errs;
  }

  private void doBind(ref, params) {
    logger.debug("Handling binding for $ref...")
    params.each { pName, pValue ->
      logger.debug("Handling param $pName, value $pValue")
      try {
        if (ref[pName]==null) {
          logger.debug("bound $ref -> $pName = $pValue (null value found)")
          ref[pName] = pValue
        } else {
          if (pValue instanceof Map) {
            doBind(ref[pName], pValue)
          } else {
            logger.debug("bound $ref -> $pName = $pValue (not null, but not a map)")
            ref[pName] = pValue
          }
        }
      } catch(MissingPropertyException e) {
        // just skip the param
        logger.debug("Skipped binding for $pName : no such property")
      }
    }
    logger.debug("Binding over for $ref")
  }

  void bind(ActionBean bean, String propertyName, Object propertyValue) {
    throw new UnsupportedOperationException("should not be invoked");
  }



}
