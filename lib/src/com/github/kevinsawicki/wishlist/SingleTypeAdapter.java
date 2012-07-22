/*
 * Copyright 2012 Kevin Sawicki <kevinsawicki@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.kevinsawicki.wishlist;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Adapter for lists where only a single view type is used
 *
 * @param <V>
 */
public abstract class SingleTypeAdapter<V> extends BaseAdapter {

  private static final Object[] EMPTY = new Object[0];

  private final LayoutInflater inflater;

  private final int layout;

  private final int[] children;

  private Object[] items;

  /**
   * Create adapter
   *
   * @param activity
   * @param layoutResourceId
   */
  public SingleTypeAdapter(final Activity activity, final int layoutResourceId) {
    this(activity.getLayoutInflater(), layoutResourceId);
  }

  /**
   * Create adapter
   *
   * @param context
   * @param layoutResourceId
   */
  public SingleTypeAdapter(final Context context, final int layoutResourceId) {
    this(LayoutInflater.from(context), layoutResourceId);
  }

  /**
   * Create adapter
   *
   * @param inflater
   * @param layoutResourceId
   */
  public SingleTypeAdapter(final LayoutInflater inflater,
      final int layoutResourceId) {
    this.inflater = inflater;
    this.layout = layoutResourceId;

    items = EMPTY;

    int[] childIds = getChildViewIds();
    if (childIds == null)
      childIds = new int[0];
    children = childIds;
  }

  /**
   * Set items to display
   *
   * @param items
   */
  public void setItems(final Object[] items) {
    if (items != null)
      this.items = items;
    else
      this.items = EMPTY;
    notifyDataSetChanged();
  }

  @Override
  public int getCount() {
    return items.length;
  }

  @SuppressWarnings("unchecked")
  public V getItem(final int position) {
    return (V) items[position];
  }

  @Override
  public long getItemId(final int position) {
    return items[position].hashCode();
  }

  /**
   * Get child view
   *
   * @param parentView
   * @param childViewId
   * @param childViewClass
   * @return child view
   */
  @SuppressWarnings("unchecked")
  protected <T> T getView(final View parentView, final int childViewId,
      final Class<T> childViewClass) {
    return (T) parentView.getTag(childViewId);
  }

  /**
   * Get text view with given id
   *
   * @param parentView
   * @param childViewId
   * @return text view
   */
  protected TextView textView(final View parentView, final int childViewId) {
    return (TextView) parentView.getTag(childViewId);
  }

  /**
   * Get image view with given id
   *
   * @param parentView
   * @param childViewId
   * @return image view
   */
  protected ImageView imageView(final View parentView, final int childViewId) {
    return (ImageView) parentView.getTag(childViewId);
  }

  /**
   * Get view with given id
   *
   * @param parentView
   * @param childViewId
   * @return view
   */
  protected View view(final View parentView, final int childViewId) {
    return (View) parentView.getTag(childViewId);
  }

  /**
   * Set text on text view with given id
   *
   * @param parentView
   * @param childViewId
   * @param text
   * @return text view
   */
  protected TextView setText(final View parentView, final int childViewId,
      final CharSequence text) {
    final TextView textView = textView(parentView, childViewId);
    textView.setText(text);
    return textView;
  }

  /**
   * Create array with given base ids and additional ids
   *
   * @param base
   * @param ids
   * @return extended array
   */
  protected int[] join(final int[] base, final int... ids) {
    if (ids == null || ids.length == 0)
      return base;

    final int[] extended = new int[base.length + ids.length];
    System.arraycopy(base, 0, extended, 0, base.length);
    System.arraycopy(ids, 0, extended, base.length, ids.length);
    return extended;
  }

  /**
   * Get child view ids to store
   *
   * @return ids
   */
  protected abstract int[] getChildViewIds();

  /**
   * Update view for item
   *
   * @param position
   * @param view
   * @param item
   */
  protected abstract void update(int position, View view, V item);

  /**
   * Initialize view
   *
   * @param view
   * @return view
   */
  protected View initialize(final View view) {
    for (int id : children) {
      View child = view.findViewById(id);
      if (child != null)
        view.setTag(id, child);
    }
    return view;
  }

  @Override
  public View getView(final int position, View convertView,
      final ViewGroup parent) {
    if (convertView == null)
      convertView = initialize(inflater.inflate(layout, null));
    update(position, convertView, getItem(position));
    return convertView;
  }
}
