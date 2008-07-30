== The "patches" directory ==

This directory contains patches that we apply to the qooxdoo codebase.

* Label.diff
  Rüdiger's Label changes, adapted to qx-0.7.3.

* Property-bug599.diff
  Patch as attached to http://bugzilla.qooxdoo.org/show_bug.cgi?id=599.
  Has been rejected by qooxdoo.


=== The "obsolete" directory ===

This directory contains outdated patches, which are not applied to qx 0.7.3.
These are only kept here for the history.

* Button-Bug239609.diff
  This patch has been applied to the qooxdoo codebase.
  Verified that bug 239609 does not appear anymore.

* exchange.diff
  Patch not applicable anymore, the Error described in qx bug 618 is obviously
  not thrown anymore.

* Label.diff
  Patch does not fully apply anymore, had to be changed slightly.

* qxbug664.diff
  Patch had already been applied to the qooxdoo codebase.

* tree_multi.diff
  The first hunk of this patch (regarding AbstractTreeElement) can not be
  applied anymore. The second one can with ~70 lines offset.
  Created a new patch that contains only the second hunk.

