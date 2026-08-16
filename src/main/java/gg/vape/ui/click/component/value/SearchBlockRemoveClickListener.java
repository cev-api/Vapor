package gg.vape.ui.click.component.value;

import gg.vape.Vapor;
import gg.vape.ui.click.component.GuiClickListener;
import gg.vape.ui.click.component.value.SearchBlockListComponent;
import gg.vape.ui.unmap.SearchBlock;

public class SearchBlockRemoveClickListener
implements GuiClickListener {
    final SearchBlock searchBlock;
    final Runnable afterRemove;
    final SearchBlockListComponent owner;

    @Override
    public void onPrimaryClick() {
        Vapor.INSTANCE.getSearch().removeSearchBlock(this.searchBlock);
        this.afterRemove.run();
    }

    public SearchBlockRemoveClickListener(SearchBlockListComponent owner, SearchBlock searchBlock, Runnable afterRemove) {
        this.owner = owner;
        this.searchBlock = searchBlock;
        this.afterRemove = afterRemove;
    }
}
