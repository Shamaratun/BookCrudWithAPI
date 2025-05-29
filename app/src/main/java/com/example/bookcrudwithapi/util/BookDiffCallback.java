package com.example.bookcrudwithapi.util;

import androidx.recyclerview.widget.DiffUtil;

import com.example.bookcrudwithapi.model.Book;

import java.util.List;
import java.util.Objects;


public class BookDiffCallback extends DiffUtil.Callback {
    private final List<Book> oldList;
    private final List<Book> newList;

    public BookDiffCallback(List<Book> oldList, List<Book> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return Objects.equals(oldList.get(oldItemPosition).getId(), newList.get(oldItemPosition).getId());
    }


    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
    return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }
    }