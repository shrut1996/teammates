'use strict';

var COMMENT_TEXT = 'commenttext';
var COMMENT_EDITTYPE = 'commentedittype';
var DISPLAY_COMMENT_BLANK = 'Please enter a valid comment. The comment can\'t be empty.';

$(document).ready(function() {

    // Auto-loading for feedback responses
    $('div[id^="studentFeedback-"]').click();

    $('a[id^="visibility-options-trigger"]').click(function() {
        var visibilityOptions = $(this).parent().next();
        if (visibilityOptions.is(':visible')) {
            visibilityOptions.hide();
            $(this).html('<span class="glyphicon glyphicon-eye-close"></span> Show Visibility Options');
        } else {
            visibilityOptions.show();
            $(this).html('<span class="glyphicon glyphicon-eye-close"></span> Hide Visibility Options');
        }
    });

    $('.panel-heading.student_feedback').click(toggleSingleCollapse);

    $('input[type=checkbox]').click(function(e) {
        var table = $(this).closest('table');
        var form = table.closest('form');
        var visibilityOptions = [];
        var target = $(e.target);
        var visibilityOptionsRow = target.closest('tr');

        if (target.prop('class').includes('answerCheckbox') && !target.prop('checked')) {
            visibilityOptionsRow.find('input[class*=giverCheckbox]').prop('checked', false);
            visibilityOptionsRow.find('input[class*=recipientCheckbox]').prop('checked', false);
        }
        if ((target.prop('class').includes('giverCheckbox') || target.prop('class').includes('recipientCheckbox'))
                && target.prop('checked')) {
            visibilityOptionsRow.find('input[class*=answerCheckbox]').prop('checked', true);
        }

        table.find('.answerCheckbox:checked').each(function() {
            visibilityOptions.push($(this).val());
        });
        form.find('input[name="showcommentsto"]').val(visibilityOptions.join(', '));

        visibilityOptions = [];
        table.find('.giverCheckbox:checked').each(function() {
            visibilityOptions.push($(this).val());
        });
        form.find('input[name="showgiverto"]').val(visibilityOptions.join(', '));

        visibilityOptions = [];
        table.find('.recipientCheckbox:checked').each(function() {
            visibilityOptions.push($(this).val());
        });
        form.find('input[name="showrecipientto"]').val(visibilityOptions.join(', '));
    });

    readyStudentRecordsPage();
});

/**
 * To be loaded when instructorStudentRecords page is loaded
 * Contains key bindings, text area adjustment and auto-opening
 * of comment box if the request parameter asks for it
 */
function readyStudentRecordsPage() {
    // Bind form submission to check for blank comment field
    $('form.form_comment').submit(function() {
        return checkComment(this);
    });

    // Adjust size of each text area, except the new comment area
    $('textarea').each(function() {
        if (!$(this).attr('placeholder')) {
            textAreaAdjust(this);
        }
    });

    // Open the comment box if so desired by the request
    if (showCommentBox === 'yes') {
        $('#button_add_comment').click();
    }
}

/**
 * Do the comment edit form submission
 * Currently done this way because the link is placed on a different column
 */
function submitCommentForm(commentIdx) {
    if ($('#' + COMMENT_EDITTYPE + '-' + commentIdx).val() !== 'delete') {
        tinymce.get('commentText' + commentIdx).save();
        $('input[name=commentText' + commentIdx + ']').attr('name', 'commenttext');
    }

    $('#form_commentedit-' + commentIdx).submit();
    return false;
}

/**
 * Check the submitted comment text field of the form
 * Blanks are not allowed.
 */
function checkComment(form) {
    var formTextField;

    if ($(form).find('[id^=' + COMMENT_EDITTYPE + ']').val() === 'delete') {
        return true;
    }

    if ($(form).attr('name') === 'form_commentadd') {
        formTextField = tinymce.get(COMMENT_TEXT).getContent();
    } else {
        var editorId = $(form).attr('id').match(/^form_commentedit-(\d+)$/)[1];
        formTextField = tinymce.get('commentText' + editorId).getContent();
    }

    if (isBlank(formTextField)) {
        setStatusMessage(DISPLAY_COMMENT_BLANK, StatusType.DANGER);
        scrollToTop();
        return false;
    }
}

/**
 * Show the comment box, focus comment text area and hide "Add Comment link"
 */
function showAddCommentBox() {
    $('#comment_box').show();

    if (typeof richTextEditorBuilder !== 'undefined') {
        /* eslint-disable camelcase */ // The property names are determined by external library (tinymce)
        richTextEditorBuilder.initEditor('#commenttext', {
            inline: true,
            fixed_toolbar_container: '#rich-text-toolbar-comment-container'
        });
        /* eslint-enable camelcase */
    }
}

function hideAddCommentBox() {
    $('#comment_box').hide();
}

/**
 * Enable the comment form indicated by index,
 * disables the others
 */
function enableEdit(commentIdx, maxComments) {
    var i = 1;
    while (i <= maxComments) {
        if (commentIdx === i) {
            enableComment(i);
        } else {
            disableComment(i);
        }
        i++;
    }
    return false;
}

function enableComment(commentIdx) {
    $('#commentBar-' + commentIdx).hide();
    $('#plainCommentText' + commentIdx).hide();
    $('div[id="commentTextEdit' + commentIdx + '"]').show();

    if (typeof richTextEditorBuilder !== 'undefined') {
        /* eslint-disable camelcase */ // The property names are determined by external library (tinymce)
        richTextEditorBuilder.initEditor('#commentText' + commentIdx, {
            inline: true,
            fixed_toolbar_container: '#rich-text-toolbar-comment-container-' + commentIdx
        });
        /* eslint-enable camelcase */
    }
}

function disableComment(commentIdx) {
    $('#commentBar-' + commentIdx).show();
    $('#plainCommentText' + commentIdx).show();
    $('div[id="commentTextEdit' + commentIdx + '"]').hide();
}

function textAreaAdjust(o) {
    var height = o.scrollHeight + 5;
    o.style.height = height + 'px';
}

/**
 * Pops up confirmation modal whether to delete specified comment
 * @param comment index
 * @returns
 */
function deleteComment(commentIdx) {
    var messageText = 'Are you sure you want to delete this comment?';
    var okCallback = function() {
        document.getElementById(COMMENT_EDITTYPE + '-' + commentIdx).value = 'delete';
        return submitCommentForm(commentIdx);
    };
    BootboxWrapper.showModalConfirmation('Confirm Deletion', messageText, okCallback, null,
                                         BootboxWrapper.DEFAULT_OK_TEXT, BootboxWrapper.DEFAULT_CANCEL_TEXT,
                                         StatusType.WARNING);
    return false;
}

function loadFeedbackSession(courseId, stuEmail, user, fsName, sender) {
    $('.tooltip').hide();
    var targetDiv = $(sender).find('div[id^="target-feedback-"]');
    var fsNameForUrl = encodeURIComponent(fsName);
    var url = '/page/instructorStudentRecordsAjaxPage?courseid=' + courseId
              + '&studentemail=' + stuEmail + '&user=' + user + '&fsname=' + fsNameForUrl;
    $(sender).find('div[class^="placeholder-img-loading"]').html('<img src="/images/ajax-loader.gif">');
    targetDiv.load(url, function(response, status) {
        if (status === 'success') {
            $(sender).removeAttr('onclick');
        }
        $(sender).find('div[class^="placeholder-img-loading"]').html('');
    });
}
