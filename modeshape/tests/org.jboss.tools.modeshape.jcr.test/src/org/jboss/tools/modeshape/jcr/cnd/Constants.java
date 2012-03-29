/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.jboss.tools.modeshape.jcr.cnd;

import java.util.Arrays;
import java.util.Collection;

import javax.jcr.PropertyType;

import org.jboss.tools.modeshape.jcr.NamespaceMapping;
import org.jboss.tools.modeshape.jcr.QualifiedName;
import org.jboss.tools.modeshape.jcr.attributes.Abstract;
import org.jboss.tools.modeshape.jcr.attributes.AttributeState;
import org.jboss.tools.modeshape.jcr.attributes.Autocreated;
import org.jboss.tools.modeshape.jcr.attributes.DefaultType;
import org.jboss.tools.modeshape.jcr.attributes.DefaultValues;
import org.jboss.tools.modeshape.jcr.attributes.Mandatory;
import org.jboss.tools.modeshape.jcr.attributes.Mixin;
import org.jboss.tools.modeshape.jcr.attributes.Multiple;
import org.jboss.tools.modeshape.jcr.attributes.NoFullText;
import org.jboss.tools.modeshape.jcr.attributes.NoQueryOrder;
import org.jboss.tools.modeshape.jcr.attributes.Orderable;
import org.jboss.tools.modeshape.jcr.attributes.PrimaryItem;
import org.jboss.tools.modeshape.jcr.attributes.PropertyValue;
import org.jboss.tools.modeshape.jcr.attributes.Protected;
import org.jboss.tools.modeshape.jcr.attributes.QueryOperators;
import org.jboss.tools.modeshape.jcr.attributes.RequiredTypes;
import org.jboss.tools.modeshape.jcr.attributes.SameNameSiblings;
import org.jboss.tools.modeshape.jcr.attributes.SuperTypes;
import org.jboss.tools.modeshape.jcr.attributes.ValueConstraints;
import org.jboss.tools.modeshape.jcr.attributes.AttributeState.Value;
import org.jboss.tools.modeshape.jcr.attributes.QueryOperators.QueryOperator;
import org.jboss.tools.modeshape.jcr.cnd.CndElement.NotationType;
import org.jboss.tools.modeshape.jcr.cnd.CndNotationPreferences.Preference;

/**
 * 
 */
public interface Constants {

    String NAMESPACE_PREFIX1 = "NAMESPACE_PREFIX1"; //$NON-NLS-1$
    String NAMESPACE_PREFIX2 = "NAMESPACE_PREFIX2"; //$NON-NLS-1$
    String NAMESPACE_PREFIX3 = "NAMESPACE_PREFIX3"; //$NON-NLS-1$
    String[] DEFAULT_NAMESPACE_PREFIXES = new String[] { NAMESPACE_PREFIX1, NAMESPACE_PREFIX2, NAMESPACE_PREFIX3 };

    String NAMESPACE_URI1 = "NAMESPACE_URI1"; //$NON-NLS-1$
    String NAMESPACE_URI2 = "NAMESPACE_URI2"; //$NON-NLS-1$
    String NAMESPACE_URI3 = "NAMESPACE_URI3"; //$NON-NLS-1$
    String[] DEFAULT_NAMESPACE_URIS = new String[] { NAMESPACE_URI1, NAMESPACE_URI2, NAMESPACE_URI3 };

    NamespaceMapping NAMESPACE1 = new NamespaceMapping(NAMESPACE_PREFIX1, NAMESPACE_URI1);
    NamespaceMapping NAMESPACE2 = new NamespaceMapping(NAMESPACE_PREFIX2, NAMESPACE_URI2);
    NamespaceMapping NAMESPACE3 = new NamespaceMapping(NAMESPACE_PREFIX3, NAMESPACE_URI3);
    NamespaceMapping[] DEFAULT_NAMESPACE_MAPPINGS = new NamespaceMapping[] { NAMESPACE1, NAMESPACE2, NAMESPACE3 };

    String QUALIFIER1 = NAMESPACE_PREFIX1;
    String QUALIFIER2 = NAMESPACE_PREFIX2;
    String QUALIFIER3 = NAMESPACE_PREFIX3;
    String[] DEFAULT_QUALIFIERS = DEFAULT_NAMESPACE_PREFIXES;

    String UNQUALIFIED_NAME1 = "UNQUALIFIED_NAME1"; //$NON-NLS-1$
    String UNQUALIFIED_NAME2 = "UNQUALIFIED_NAME2"; //$NON-NLS-1$
    String UNQUALIFIED_NAME3 = "UNQUALIFIED_NAME3"; //$NON-NLS-1$
    String[] DEFAULT_UNQUALIFIED_NAMES = new String[] { UNQUALIFIED_NAME1, UNQUALIFIED_NAME2, UNQUALIFIED_NAME3 };

    QualifiedName QUALIFIED_NAME1 = new QualifiedName(QUALIFIER1, UNQUALIFIED_NAME1);
    QualifiedName QUALIFIED_NAME2 = new QualifiedName(QUALIFIER2, UNQUALIFIED_NAME2);
    QualifiedName QUALIFIED_NAME3 = new QualifiedName(QUALIFIER3, UNQUALIFIED_NAME3);
    QualifiedName[] DEFAULT_QUALIFIED_NAMES = new QualifiedName[] { QUALIFIED_NAME1, QUALIFIED_NAME2, QUALIFIED_NAME3 };
    QualifiedName NAME_WITH_EMPTY_QUALIFIER = new QualifiedName(null, UNQUALIFIED_NAME1);
    QualifiedName NAME_WITH_NON_DEFAULT_QUALIFIER = new QualifiedName(QUALIFIER1 + "changed", UNQUALIFIED_NAME1); //$NON-NLS-1$

    String VARIANT = AttributeState.VARIANT_STRING;

    String ABSTRACT_VARIANT_COMPACT_FORM = Abstract.NOTATION[NotationType.COMPACT_INDEX] + VARIANT;
    String ABSTRACT_VARIANT_COMPRESSED_FORM = Abstract.NOTATION[NotationType.COMPRESSED_INDEX] + VARIANT;
    String ABSTRACT_VARIANT_LONG_FORM = Abstract.NOTATION[NotationType.LONG_INDEX] + VARIANT;

    String AUTOCREATED_VARIANT_COMPACT_FORM = Autocreated.NOTATION[NotationType.COMPACT_INDEX] + VARIANT;
    String AUTOCREATED_VARIANT_COMPRESSED_FORM = Autocreated.NOTATION[NotationType.COMPRESSED_INDEX] + VARIANT;
    String AUTOCREATED_VARIANT_LONG_FORM = Autocreated.NOTATION[NotationType.LONG_INDEX] + VARIANT;

    String DEFAULT_TYPE_VARIANT_FORM = DefaultType.NOTATION
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.DEFAULT_TYPE_END_PREFIX_DELIMITER) + VARIANT;
    String DEFAULT_TYPE = "jcr:data"; //$NON-NLS-1$
    String DEFAULT_TYPE_TYPE_FORM = DefaultType.NOTATION
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.DEFAULT_TYPE_END_PREFIX_DELIMITER) + DEFAULT_TYPE;

    String MANDATORY_VARIANT_COMPACT_FORM = Mandatory.NOTATION[NotationType.COMPACT_INDEX] + VARIANT;
    String MANDATORY_VARIANT_COMPRESSED_FORM = Mandatory.NOTATION[NotationType.COMPRESSED_INDEX] + VARIANT;
    String MANDATORY_VARIANT_LONG_FORM = Mandatory.NOTATION[NotationType.LONG_INDEX] + VARIANT;

    String MIXIN_VARIANT_COMPACT_FORM = Mixin.NOTATION[NotationType.COMPACT_INDEX] + VARIANT;
    String MIXIN_VARIANT_COMPRESSED_FORM = Mixin.NOTATION[NotationType.COMPRESSED_INDEX] + VARIANT;
    String MIXIN_VARIANT_LONG_FORM = Mixin.NOTATION[NotationType.LONG_INDEX] + VARIANT;

    String MULTIPLE_VARIANT_COMPACT_FORM = Multiple.NOTATION[NotationType.COMPACT_INDEX] + VARIANT;
    String MULTIPLE_VARIANT_COMPRESSED_FORM = Multiple.NOTATION[NotationType.COMPRESSED_INDEX] + VARIANT;
    String MULTIPLE_VARIANT_LONG_FORM = Multiple.NOTATION[NotationType.LONG_INDEX] + VARIANT;

    String NO_FULL_TEXT_VARIANT_COMPACT_FORM = NoFullText.NOTATION[NotationType.COMPACT_INDEX] + VARIANT;
    String NO_FULL_TEXT_VARIANT_COMPRESSED_FORM = NoFullText.NOTATION[NotationType.COMPRESSED_INDEX] + VARIANT;
    String NO_FULL_TEXT_VARIANT_LONG_FORM = NoFullText.NOTATION[NotationType.LONG_INDEX] + VARIANT;

    String NO_QUERY_ORDER_VARIANT_COMPACT_FORM = NoQueryOrder.NOTATION[NotationType.COMPACT_INDEX] + VARIANT;
    String NO_QUERY_ORDER_VARIANT_COMPRESSED_FORM = NoQueryOrder.NOTATION[NotationType.COMPRESSED_INDEX] + VARIANT;
    String NO_QUERY_ORDER_VARIANT_LONG_FORM = NoQueryOrder.NOTATION[NotationType.LONG_INDEX] + VARIANT;

    String ORDERABLE_VARIANT_COMPACT_FORM = Orderable.NOTATION[NotationType.COMPACT_INDEX] + VARIANT;
    String ORDERABLE_VARIANT_COMPRESSED_FORM = Orderable.NOTATION[NotationType.COMPRESSED_INDEX] + VARIANT;
    String ORDERABLE_VARIANT_LONG_FORM = Orderable.NOTATION[NotationType.LONG_INDEX] + VARIANT;

    String PRIMARY_ITEM_VARIANT_COMPACT_FORM = PrimaryItem.NOTATION[NotationType.COMPACT_INDEX] + ' ' + VARIANT;
    String PRIMARY_ITEM_VARIANT_COMPRESSED_FORM = PrimaryItem.NOTATION[NotationType.COMPRESSED_INDEX] + ' ' + VARIANT;
    String PRIMARY_ITEM_VARIANT_LONG_FORM = PrimaryItem.NOTATION[NotationType.LONG_INDEX] + ' ' + VARIANT;
    String PRIMARY_ITEM = "jcr:data"; //$NON-NLS-1$
    String PRIMARY_ITEM_ITEM_COMPACT_FORM = PrimaryItem.NOTATION[NotationType.COMPACT_INDEX] + ' ' + PRIMARY_ITEM;
    String PRIMARY_ITEM_ITEM_COMPRESSED_FORM = PrimaryItem.NOTATION[NotationType.COMPRESSED_INDEX] + ' ' + PRIMARY_ITEM;
    String PRIMARY_ITEM_ITEM_LONG_FORM = PrimaryItem.NOTATION[NotationType.LONG_INDEX] + ' ' + PRIMARY_ITEM;

    String PROTECTED_VARIANT_COMPACT_FORM = Protected.NOTATION[NotationType.COMPACT_INDEX] + VARIANT;
    String PROTECTED_VARIANT_COMPRESSED_FORM = Protected.NOTATION[NotationType.COMPRESSED_INDEX] + VARIANT;
    String PROTECTED_VARIANT_LONG_FORM = Protected.NOTATION[NotationType.LONG_INDEX] + VARIANT;

    String QUERY_OPS_COMPACT_FORM = QueryOperators.NOTATION[NotationType.COMPACT_INDEX]
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_PREFIX_END_DELIMITER);
    String QUERY_OPS_COMPRESSED_FORM = QueryOperators.NOTATION[NotationType.COMPRESSED_INDEX]
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_PREFIX_END_DELIMITER);
    String QUERY_OPS_LONG_FORM = QueryOperators.NOTATION[NotationType.LONG_INDEX]
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_PREFIX_END_DELIMITER);
    String QUERY_OPS_VARIANT_COMPACT_FORM = QUERY_OPS_COMPACT_FORM + VARIANT;
    String QUERY_OPS_VARIANT_COMPRESSED_FORM = QUERY_OPS_COMPRESSED_FORM + VARIANT;
    String QUERY_OPS_VARIANT_LONG_FORM = QUERY_OPS_LONG_FORM + VARIANT;

    QueryOperator OPERATOR_ONE = QueryOperator.EQUALS;
    QueryOperator OPERATOR_TWO = QueryOperator.GREATER_THAN;
    QueryOperator OPERATOR_THREE = QueryOperator.LESS_THAN;
    QueryOperator[] DEFAULT_OPERATORS = new QueryOperator[] { OPERATOR_ONE, OPERATOR_TWO, OPERATOR_THREE };

    String QUERY_OPS_ONE_OPERATOR_COMPACT_FORM = QUERY_OPS_COMPACT_FORM
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ITEM_QUOTE_CHAR)
            + OPERATOR_ONE.toCndNotation(NotationType.COMPACT)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ITEM_QUOTE_CHAR);
    String QUERY_OPS_ONE_OPERATOR_COMPRESSED_FORM = QUERY_OPS_COMPRESSED_FORM
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ITEM_QUOTE_CHAR)
            + OPERATOR_ONE.toCndNotation(NotationType.COMPRESSED)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ITEM_QUOTE_CHAR);
    String QUERY_OPS_ONE_OPERATOR_LONG_FORM = QUERY_OPS_LONG_FORM
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ITEM_QUOTE_CHAR)
            + OPERATOR_ONE.toCndNotation(NotationType.LONG)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ITEM_QUOTE_CHAR);
    String QUERY_OPS_THREE_OPERATOR_COMPACT_FORM = QUERY_OPS_COMPACT_FORM
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ITEM_QUOTE_CHAR)
            + OPERATOR_ONE.toCndNotation(NotationType.COMPACT)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ELEMENT_DELIMITER)
            + OPERATOR_TWO.toCndNotation(NotationType.COMPACT)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ELEMENT_DELIMITER)
            + OPERATOR_THREE.toCndNotation(NotationType.COMPACT)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ITEM_QUOTE_CHAR);
    String QUERY_OPS_THREE_OPERATOR_COMPRESSED_FORM = QUERY_OPS_COMPRESSED_FORM
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ITEM_QUOTE_CHAR)
            + OPERATOR_ONE.toCndNotation(NotationType.COMPRESSED)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ELEMENT_DELIMITER)
            + OPERATOR_TWO.toCndNotation(NotationType.COMPRESSED)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ELEMENT_DELIMITER)
            + OPERATOR_THREE.toCndNotation(NotationType.COMPRESSED)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ITEM_QUOTE_CHAR);
    String QUERY_OPS_THREE_OPERATOR_LONG_FORM = QUERY_OPS_LONG_FORM
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ITEM_QUOTE_CHAR)
            + OPERATOR_ONE.toCndNotation(NotationType.LONG)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ELEMENT_DELIMITER)
            + OPERATOR_TWO.toCndNotation(NotationType.LONG)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ELEMENT_DELIMITER)
            + OPERATOR_THREE.toCndNotation(NotationType.LONG)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ITEM_QUOTE_CHAR);

    String SAME_NAME_SIBLINGS_VARIANT_COMPACT_FORM = SameNameSiblings.NOTATION[NotationType.COMPACT_INDEX] + VARIANT;
    String SAME_NAME_SIBLINGS_VARIANT_COMPRESSED_FORM = SameNameSiblings.NOTATION[NotationType.COMPRESSED_INDEX] + VARIANT;
    String SAME_NAME_SIBLINGS_VARIANT_LONG_FORM = SameNameSiblings.NOTATION[NotationType.LONG_INDEX] + VARIANT;

    String ITEM_ONE = "item1"; //$NON-NLS-1$ 
    String ITEM_TWO = "item2"; //$NON-NLS-1$ 
    String ITEM_THREE = "item3"; //$NON-NLS-1$
    String ONE_ITEM_SINGLE_QUOTED_FORM = CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_QUOTE_CHAR)
            + ITEM_ONE + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_QUOTE_CHAR);
    String THREE_ITEM_SINGLE_QUOTED_FORM = CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_QUOTE_CHAR)
            + ITEM_ONE + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ELEMENT_DELIMITER) + ITEM_TWO
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ELEMENT_DELIMITER) + ITEM_THREE
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_QUOTE_CHAR);

    String DEFAULT_VALUES_VARIANT = DefaultValues.NOTATION_PREFIX
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.DEFAULT_VALUES_END_PREFIX_DELIMITER) + VARIANT;
    String DEFAULT_VALUES_ONE_ITEM_FORM = DefaultValues.NOTATION_PREFIX
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.DEFAULT_VALUES_END_PREFIX_DELIMITER)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.DEFAULT_VALUES_QUOTE_CHARACTER)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ITEM_QUOTE_CHAR) + ITEM_ONE
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ITEM_QUOTE_CHAR)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.DEFAULT_VALUES_QUOTE_CHARACTER);
    String DEFAULT_VALUES_THREE_ITEM_FORM = DefaultValues.NOTATION_PREFIX
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.DEFAULT_VALUES_END_PREFIX_DELIMITER)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.DEFAULT_VALUES_QUOTE_CHARACTER)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ITEM_QUOTE_CHAR) + ITEM_ONE
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ITEM_QUOTE_CHAR)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ELEMENT_DELIMITER)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ITEM_QUOTE_CHAR) + ITEM_TWO
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ITEM_QUOTE_CHAR)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ELEMENT_DELIMITER)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ITEM_QUOTE_CHAR) + ITEM_THREE
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ITEM_QUOTE_CHAR)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.DEFAULT_VALUES_QUOTE_CHARACTER);

    String REQUIRED_TYPES_VARIANT = RequiredTypes.NOTATION_PREFIX + VARIANT + RequiredTypes.NOTATION_SUFFIX;
    String REQUIRED_TYPES_ONE_ITEM_FORM = RequiredTypes.NOTATION_PREFIX + QUALIFIED_NAME1 + RequiredTypes.NOTATION_SUFFIX;
    String REQUIRED_TYPES_THREE_ITEM_FORM = RequiredTypes.NOTATION_PREFIX + QUALIFIED_NAME1
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ELEMENT_DELIMITER) + QUALIFIED_NAME2
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ELEMENT_DELIMITER) + QUALIFIED_NAME3
            + RequiredTypes.NOTATION_SUFFIX;

    String SUPER_TYPES_VARIANT = SuperTypes.NOTATION_PREFIX
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_PREFIX_END_DELIMITER) + VARIANT;
    String SUPER_TYPES_ONE_ITEM_FORM = SuperTypes.NOTATION_PREFIX
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_PREFIX_END_DELIMITER) + QUALIFIED_NAME1;
    String SUPER_TYPES_THREE_ITEM_FORM = SuperTypes.NOTATION_PREFIX
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_PREFIX_END_DELIMITER) + QUALIFIED_NAME1
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ELEMENT_DELIMITER) + QUALIFIED_NAME2
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ELEMENT_DELIMITER) + QUALIFIED_NAME3;

    String VALUE_CONSTRAINT1 = "(19|20)\\d{2}"; //$NON-NLS-1$
    String VALUE_CONSTRAINT2 = "[$]\\d{1,3}[,]?\\d{3}([.]\\d{2})?"; //$NON-NLS-1$
    String VALUE_CONSTRAINT3 = "[1,5]"; //$NON-NLS-1$
    String[] DEFAULT_VALUE_CONSTRAINTS = new String[] {VALUE_CONSTRAINT1, VALUE_CONSTRAINT2, VALUE_CONSTRAINT3};
    
    String VALUE_CONSTRAINTS_VARIANT = ValueConstraints.NOTATION_PREFIX
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_PREFIX_END_DELIMITER) + VARIANT;
    String VALUE_CONSTRAINTS_ONE_ITEM_FORM = ValueConstraints.NOTATION_PREFIX
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_PREFIX_END_DELIMITER)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_QUOTE_CHAR)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.VALUE_CONSTRAINTS_ITEM_QUOTE_CHARACTER) + VALUE_CONSTRAINT1
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.VALUE_CONSTRAINTS_ITEM_QUOTE_CHARACTER)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_QUOTE_CHAR);
    String VALUE_CONSTRAINTS_THREE_ITEM_FORM = ValueConstraints.NOTATION_PREFIX
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_PREFIX_END_DELIMITER)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_QUOTE_CHAR)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.VALUE_CONSTRAINTS_ITEM_QUOTE_CHARACTER) + VALUE_CONSTRAINT1
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.VALUE_CONSTRAINTS_ITEM_QUOTE_CHARACTER)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ELEMENT_DELIMITER)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.VALUE_CONSTRAINTS_ITEM_QUOTE_CHARACTER) + VALUE_CONSTRAINT2
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.VALUE_CONSTRAINTS_ITEM_QUOTE_CHARACTER)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ELEMENT_DELIMITER)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.VALUE_CONSTRAINTS_ITEM_QUOTE_CHARACTER) + VALUE_CONSTRAINT3
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.VALUE_CONSTRAINTS_ITEM_QUOTE_CHARACTER)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_QUOTE_CHAR);

    class Helper {
        public static void changeValue( AttributeState attribute ) {
            if (attribute.is()) {
                attribute.set(Value.IS_NOT);
            } else if (attribute.isNot()) {
                attribute.set(Value.IS);
            } else {
                attribute.set(Value.IS);
            }
        }

        public static Collection<NamespaceMapping> getDefaultNamespaces() {
            return Arrays.asList(DEFAULT_NAMESPACE_MAPPINGS);
        }

        public static Collection<QualifiedName> getDefaultQualifiedNames() {
            return Arrays.asList(DEFAULT_QUALIFIED_NAMES);
        }

        public static String[] getDefaultQualifiedNamesAsStringArray() {
            String[] names = new String[DEFAULT_QUALIFIED_NAMES.length];
            int i = 0;
            
            for (QualifiedName qname : DEFAULT_QUALIFIED_NAMES) {
                names[i++] = qname.get();
            }

            return names;
        }

        public static Collection<String> getDefaultNamespacePrefixes() {
            return Arrays.asList(DEFAULT_NAMESPACE_PREFIXES);
        }

        public static Collection<String> getDefaultQualifiers() {
            return Arrays.asList(DEFAULT_QUALIFIERS);
        }

        public static String[] getDefaultQueryOperators() {
            String[] result = new String[DEFAULT_OPERATORS.length];
            int i = 0;

            for (QueryOperator operator : DEFAULT_OPERATORS) {
                result[i++] = operator.toString();
            }

            return result;
        }

        public static javax.jcr.Value[] getDefaultStringValues() {
            return new PropertyValue[] { new PropertyValue(PropertyType.STRING, ITEM_ONE),
                    new PropertyValue(PropertyType.STRING, ITEM_TWO), new PropertyValue(PropertyType.STRING, ITEM_THREE) };
        }
    }
}
