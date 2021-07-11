'use strict';

var getBusinessObject = require('bpmn-js/lib/util/ModelUtil').getBusinessObject,
    cmdHelper = require('../helper/CmdHelper'),
    escapeHTML = require('../Utils').escapeHTML;

var entryFieldDescription = require('./EntryFieldDescription');


var checkbox = function (options, defaultParameters) {
    var resource = defaultParameters,
        id = resource.id,
        label = options.label || id,
        canBeDisabled = !!options.disabled && typeof options.disabled === 'function',
        canBeHidden = !!options.hidden && typeof options.hidden === 'function',
        description = options.description;

    resource.html =
        '<input id="activiti-' + escapeHTML(id) + '" ' +
        'type="checkbox" ' +
        'name="' + escapeHTML(options.modelProperty) + '" ' +
        (canBeDisabled ? 'data-disable="isDisabled"' : '') +
        (canBeHidden ? 'data-show="isHidden"' : '') +
        ' />' +
        '<label for="activiti-' + escapeHTML(id) + '" ' +
        (canBeDisabled ? 'data-disable="isDisabled"' : '') +
        (canBeHidden ? 'data-show="isHidden"' : '') +
        '>' + escapeHTML(label) + '</label>';

    // add description below checkbox entry field
    if (description) {
        resource.html += entryFieldDescription(description);
    }

    resource.get = function (element) {
        let bo = getBusinessObject(element),
            result = {};

        result[options.modelProperty] = bo.get(options.modelProperty);

        return result;
    };

    resource.set = function (element, values) {
        let result = {};

        result[options.modelProperty] = !!values[options.modelProperty];

        return cmdHelper.updateProperties(element, result);
    };

    if (typeof options.set === 'function') {
        resource.set = options.set;
    }

    if (typeof options.get === 'function') {
        resource.get = options.get;
    }

    if (canBeDisabled) {
        resource.isDisabled = function () {
            return options.disabled.apply(resource, arguments);
        };
    }

    if (canBeHidden) {
        resource.isHidden = function () {
            return !options.hidden.apply(resource, arguments);
        };
    }

    resource.cssClasses = ['bpp-checkbox'];

    return resource;
};

module.exports = checkbox;
