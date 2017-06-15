package com.huangyu.mdfolder.mvp.presenter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;

import com.huangyu.library.mvp.BasePresenter;
import com.huangyu.mdfolder.R;
import com.huangyu.mdfolder.app.Constants;
import com.huangyu.mdfolder.mvp.model.FileListModel;
import com.huangyu.mdfolder.mvp.model.FileModel;
import com.huangyu.mdfolder.mvp.view.IFileListView;

import java.io.File;
import java.util.List;
import java.util.Stack;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func3;
import rx.observables.GroupedObservable;
import rx.schedulers.Schedulers;

/**
 * Created by huangyu on 2017/5/22.
 */

public class FileListPresenter extends BasePresenter<IFileListView> {

    private FileListModel mFileListModel;
    private FileModel mFileModel;
    private Stack<File> mFileStack;

    private String mCurrentPath; // 当前路径
    public int mEditType;   // 当前编辑状态
    public int mFileType;   // 当前文件类型

    @Override
    public void create() {
        mFileListModel = new FileListModel();
        mFileModel = new FileModel();
        mFileStack = new Stack<>();
        mEditType = Constants.EditType.NONE;
        mFileType = Constants.FileType.FILE;
    }

    /**
     * 获取根目录文件列表
     */
    public void onLoadRootFileList(final String searchStr) {
        Subscription subscription = Observable.defer(new Func0<Observable<List<File>>>() {
            @Override
            public Observable<List<File>> call() {
                return Observable.just(getCurrentFileList(searchStr));
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mCurrentPath = mFileListModel.getRootPath();
                        mFileStack.clear();
                        mFileStack.push(new File(mCurrentPath));
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<File>>() {
                    @Override
                    public void onStart() {
                        mView.startRefresh();
                        mView.finishAction();
                    }

                    @Override
                    public void onNext(List<File> fileList) {
                        mView.removeAllTabs();
                        mView.addTab(mCurrentPath);
                        mView.refreshData(fileList, true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showSnack(e.toString());
                    }

                    @Override
                    public void onCompleted() {
                        mView.stopRefresh();
                    }
                });
        mRxManager.add(subscription);
    }

    /**
     * 获取存储器文件列表
     */
    public void onLoadStorageFileList(final String searchStr) {
        Subscription subscription = Observable.defer(new Func0<Observable<List<File>>>() {
            @Override
            public Observable<List<File>> call() {
                return Observable.just(getCurrentFileList(searchStr));
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mCurrentPath = mFileListModel.getSDCardPath();
                        mFileStack.clear();
                        mFileStack.push(new File(mCurrentPath));
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<File>>() {
                    @Override
                    public void onStart() {
                        mView.startRefresh();
                        mView.finishAction();
                    }

                    @Override
                    public void onNext(List<File> fileList) {
                        mView.removeAllTabs();
                        mView.addTab(mCurrentPath);
                        mView.refreshData(fileList, true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showSnack(e.toString());
                    }

                    @Override
                    public void onCompleted() {
                        mView.stopRefresh();
                    }
                });
        mRxManager.add(subscription);
    }

    /**
     * 获取下载文件列表
     */
    public void onLoadDownloadFileList(final String searchStr) {
        Subscription subscription = Observable.defer(new Func0<Observable<List<File>>>() {
            @Override
            public Observable<List<File>> call() {
                return Observable.just(getCurrentFileList(searchStr));
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mCurrentPath = mFileListModel.getDownloadPath();
                        mFileStack.clear();
                        mFileStack.push(new File(mCurrentPath));
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<File>>() {
                    @Override
                    public void onStart() {
                        mView.startRefresh();
                        mView.finishAction();
                    }

                    @Override
                    public void onNext(List<File> fileList) {
                        mView.removeAllTabs();
                        mView.addTab(mCurrentPath);
                        mView.refreshData(fileList, true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showSnack(e.toString());
                    }

                    @Override
                    public void onCompleted() {
                        mView.stopRefresh();
                    }
                });
        mRxManager.add(subscription);
    }

    /**
     * 获取不同类型文件列表
     */
    public void onLoadMultiTypeFileList(final String searchStr, final int fileType) {
        Subscription subscription = Observable.defer(new Func0<Observable<List<File>>>() {
            @Override
            public Observable<List<File>> call() {
                return Observable.just(getCurrentFileList(searchStr));
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mFileStack.clear();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<File>>() {
                    @Override
                    public void onStart() {
                        mView.startRefresh();
                        mView.finishAction();
                    }

                    @Override
                    public void onNext(List<File> fileList) {
                        mView.removeAllTabs();
                        switch (fileType) {
                            case Constants.FileType.APPS:
                                mView.addTab(mView.getResString(R.string.menu_apps));
                                break;
                            case Constants.FileType.PHOTO:
                                mView.addTab(mView.getResString(R.string.menu_photo));
                                break;
                            case Constants.FileType.MUSIC:
                                mView.addTab(mView.getResString(R.string.menu_music));
                                break;
                            case Constants.FileType.VIDEO:
                                mView.addTab(mView.getResString(R.string.menu_video));
                                break;
                        }
                        mView.refreshData(fileList, true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showSnack(e.toString());
                    }

                    @Override
                    public void onCompleted() {
                        mView.stopRefresh();
                    }
                });
        mRxManager.add(subscription);
    }

    /**
     * 刷新界面
     */
    public void onRefresh(final String searchStr, final boolean ifClearSelected) {
        Subscription subscription = Observable.defer(new Func0<Observable<List<File>>>() {
            @Override
            public Observable<List<File>> call() {
                return Observable.just(getCurrentFileList(searchStr));
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<File>>() {
                    @Override
                    public void onStart() {
                        mView.startRefresh();
                    }

                    @Override
                    public void onNext(List<File> fileList) {
                        mView.refreshData(fileList, ifClearSelected);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showSnack(e.toString());
                    }

                    @Override
                    public void onCompleted() {
                        if (mEditType == Constants.EditType.NONE) {
                            mView.finishAction();
                        }
                        mView.stopRefresh();
                    }
                });
        mRxManager.add(subscription);
    }

    /**
     * 新增文件
     */
    public void onAddFile() {
        if (mFileType != Constants.FileType.FILE) {
            mView.showSnack(mView.getResString(R.string.tips_add_file_error));
            return;
        }

        final View view = mView.inflateAlertDialogLayout();
        final EditText editText = mView.findAlertDialogEditText(view);
        mView.showKeyboard(mView.findAlertDialogEditText(view));
        mView.showAlert(view, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Subscription subscription = Observable.just(editText.getText().toString())
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(Schedulers.io())
                        .map(new Func1<String, String>() {
                            @Override
                            public String call(String fileName) {
                                return mCurrentPath + File.separator + fileName;
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .flatMap(new Func1<String, Observable<String>>() {
                            @Override
                            public Observable<String> call(String filePath) {
                                Observable<Boolean> observable1 = Observable.just(filePath).exists(new Func1<String, Boolean>() {
                                    @Override
                                    public Boolean call(String filePath) {
                                        return isFileExists(filePath);
                                    }
                                });

                                Observable<Boolean> observable2 = Observable.just(filePath).exists(new Func1<String, Boolean>() {
                                    @Override
                                    public Boolean call(String filePath) {
                                        return isFolderExists(filePath);
                                    }
                                });

                                Observable<String> observable3 = Observable.just(filePath);

                                return Observable.zip(observable1, observable2, observable3, new Func3<Boolean, Boolean, String, String>() {
                                    @Override
                                    public String call(Boolean isFileExists, Boolean isFolderExists, String filePath) {
                                        if (isFileExists) {
                                            mView.showSnack(mView.getResString(R.string.tips_file_exist));
                                        } else if (isFolderExists) {
                                            mView.showSnack(mView.getResString(R.string.tips_folder_exist));
                                        } else {
                                            return filePath;
                                        }
                                        return null;
                                    }
                                });
                            }
                        })
                        .subscribe(new Subscriber<String>() {
                            @Override
                            public void onNext(String filePath) {
                                if (filePath == null) {
                                    return;
                                }
                                if (addFile(filePath)) {
                                    mView.showSnack(mView.getResString(R.string.tips_add_file_successfully));
                                    mView.refreshData(false);
                                } else {
                                    mView.showSnack(mView.getResString(R.string.tips_add_file_error));
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                mView.showSnack(e.toString());
                            }

                            @Override
                            public void onCompleted() {
                                mView.hideKeyboard(mView.findAlertDialogEditText(view));
                                mView.closeFloatingActionMenu();
                            }
                        });
                mRxManager.add(subscription);
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 新增文件夹
     */
    public void onAddFolder() {
        if (mFileType != Constants.FileType.FILE) {
            mView.showSnack(mView.getResString(R.string.tips_add_folder_error));
            return;
        }

        final View view = mView.inflateAlertDialogLayout();
        final EditText editText = mView.findAlertDialogEditText(view);
        mView.showKeyboard(mView.findAlertDialogEditText(view));
        mView.showAlert(view, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Subscription subscription = Observable.just(editText.getText().toString())
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(Schedulers.io())
                        .map(new Func1<String, String>() {
                            @Override
                            public String call(String fileName) {
                                return mCurrentPath + File.separator + fileName;
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .flatMap(new Func1<String, Observable<String>>() {
                            @Override
                            public Observable<String> call(String filePath) {
                                Observable<Boolean> observable1 = Observable.just(filePath).exists(new Func1<String, Boolean>() {
                                    @Override
                                    public Boolean call(String filePath) {
                                        return isFileExists(filePath);
                                    }
                                });

                                Observable<Boolean> observable2 = Observable.just(filePath).exists(new Func1<String, Boolean>() {
                                    @Override
                                    public Boolean call(String filePath) {
                                        return isFolderExists(filePath);
                                    }
                                });

                                Observable<String> observable3 = Observable.just(filePath);

                                return Observable.zip(observable1, observable2, observable3, new Func3<Boolean, Boolean, String, String>() {
                                    @Override
                                    public String call(Boolean isFileExists, Boolean isFolderExists, String filePath) {
                                        if (isFileExists) {
                                            mView.showSnack(mView.getResString(R.string.tips_file_exist));
                                        } else if (isFolderExists) {
                                            mView.showSnack(mView.getResString(R.string.tips_folder_exist));
                                        } else {
                                            return filePath;
                                        }
                                        return null;
                                    }
                                });
                            }
                        })
                        .subscribe(new Subscriber<String>() {
                            @Override
                            public void onNext(String filePath) {
                                if (filePath == null) {
                                    return;
                                }
                                if (addFolder(filePath)) {
                                    mView.showSnack(mView.getResString(R.string.tips_add_folder_successfully));
                                    mView.refreshData(false);
                                } else {
                                    mView.showSnack(mView.getResString(R.string.tips_add_folder_error));
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                mView.showSnack(e.toString());
                            }

                            @Override
                            public void onCompleted() {
                                mView.hideKeyboard(mView.findAlertDialogEditText(view));
                                mView.closeFloatingActionMenu();
                            }
                        });
                mRxManager.add(subscription);
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 删除文件
     */
    public void onDelete(final List<File> fileList) {
        mView.showNormalAlert(mView.getResString(R.string.tips_delete_files), mView.getResString(R.string.act_delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Subscription subscription = Observable.from(fileList).groupBy(new Func1<File, Boolean>() {
                    @Override
                    public Boolean call(File file) {
                        return file.isDirectory();
                    }
                }).subscribe(new Action1<GroupedObservable<Boolean, File>>() {
                    @Override
                    public void call(final GroupedObservable<Boolean, File> o) {
                        Subscription subscription = o.all(new Func1<File, Boolean>() {
                            @Override
                            public Boolean call(File file) {
                                boolean result;
                                if (o.getKey()) {
                                    result = deleteFolder(file.getPath());
                                } else {
                                    result = deleteFile(file.getPath());
                                }
                                return result;
                            }
                        }).subscribe(new Subscriber<Boolean>() {
                            @Override
                            public void onNext(Boolean result) {
                                if (result) {
                                    mView.showSnack(mView.getResString(R.string.tips_delete_successfully));
                                } else {
                                    mView.showSnack(mView.getResString(R.string.tips_delete_in_error));
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                mView.showSnack(e.toString());
                            }

                            @Override
                            public void onCompleted() {
                                mView.finishAction();
                            }
                        });
                        mRxManager.add(subscription);
                    }
                });
                mRxManager.add(subscription);
            }
        });
    }

    /**
     * 复制文件
     */
    public void onCopy(final List<File> fileList) {
        mView.showNormalAlert(mView.getResString(R.string.tips_copy_files), mView.getResString(R.string.act_copy), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Subscription subscription = Observable.from(fileList).groupBy(new Func1<File, Boolean>() {
                    @Override
                    public Boolean call(File file) {
                        return file.isDirectory();
                    }
                }).subscribe(new Action1<GroupedObservable<Boolean, File>>() {
                    @Override
                    public void call(final GroupedObservable<Boolean, File> o) {
                        Subscription subscription = o.all(new Func1<File, Boolean>() {
                            @Override
                            public Boolean call(File file) {
                                boolean result;
                                if (o.getKey()) {
                                    result = copyFolder(file.getPath(), mCurrentPath + File.separator + file.getName());
                                } else {
                                    result = copyFile(file.getPath(), mCurrentPath + File.separator + file.getName());
                                }
                                return result;
                            }
                        }).subscribe(new Subscriber<Boolean>() {
                            @Override
                            public void onNext(Boolean result) {
                                if (result) {
                                    mView.showSnack(mView.getResString(R.string.tips_copy_successfully));
                                } else {
                                    mView.showSnack(mView.getResString(R.string.tips_copy_in_error));
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                mView.showSnack(e.toString());
                            }

                            @Override
                            public void onCompleted() {
                                mView.finishAction();
                            }
                        });
                        mRxManager.add(subscription);
                    }
                });
                mRxManager.add(subscription);
            }
        });
    }

    /**
     * 剪切文件
     */
    public void onCut(final List<File> fileList) {
        mView.showNormalAlert(mView.getResString(R.string.tips_cut_files), mView.getResString(R.string.act_cut), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Subscription subscription = Observable.from(fileList).groupBy(new Func1<File, Boolean>() {
                    @Override
                    public Boolean call(File file) {
                        return file.isDirectory();
                    }
                }).subscribe(new Action1<GroupedObservable<Boolean, File>>() {
                    @Override
                    public void call(final GroupedObservable<Boolean, File> o) {
                        Subscription subscription = o.all(new Func1<File, Boolean>() {
                            @Override
                            public Boolean call(File file) {
                                boolean result;
                                if (o.getKey()) {
                                    result = cutFolder(file.getPath(), mCurrentPath + File.separator + file.getName());
                                } else {
                                    result = cutFile(file.getPath(), mCurrentPath + File.separator + file.getName());
                                }
                                return result;
                            }
                        }).subscribe(new Subscriber<Boolean>() {
                            @Override
                            public void onNext(Boolean result) {
                                if (result) {
                                    mView.showSnack(mView.getResString(R.string.tips_cut_successfully));
                                } else {
                                    mView.showSnack(mView.getResString(R.string.tips_cut_in_error));
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                mView.showSnack(e.toString());
                            }

                            @Override
                            public void onCompleted() {
                                mView.finishAction();
                            }
                        });
                        mRxManager.add(subscription);
                    }
                });
                mRxManager.add(subscription);
            }
        });
    }

    /**
     * 获取当前路径文件列表
     *
     * @param searchStr 查询文字
     * @return 当前路径文件列表
     */
    public List<File> getCurrentFileList(String searchStr) {
        switch (mFileType) {
            case Constants.FileType.FILE:
            case Constants.FileType.DOWNLOAD:
                break;
            case Constants.FileType.APPS:
                return mFileListModel.orderByType(mFileListModel.getAppsFileList(searchStr));
            case Constants.FileType.MUSIC:
                return mFileListModel.orderByType(mFileListModel.getMusicFileList(searchStr));
            case Constants.FileType.PHOTO:
                return mFileListModel.orderByType(mFileListModel.getPhotoFileList(searchStr));
            case Constants.FileType.VIDEO:
                return mFileListModel.orderByType(mFileListModel.getVideoFileList(searchStr));
        }
        return mFileListModel.orderByType(mFileListModel.getFileList(mCurrentPath, searchStr));
    }

    /**
     * 进入某个文件夹
     *
     * @param file 文件夹
     */
    public void enterFolder(File file) {
        mFileStack.push(file);
        mView.addTab(file.getName());
        mCurrentPath = file.getPath();
        mView.refreshData(false);
    }

    /**
     * 点击路径进入某个文件夹
     *
     * @param index 文件层级
     * @return 是否tab被移除
     */
    public boolean enterCertainFolder(int index) {
        boolean isRemoved = false;
        while (mFileStack.size() > index + 1) {
            mFileStack.pop();
            mView.removeTab();
            isRemoved = true;
        }
        if (isRemoved) {
            File file = mFileStack.peek();
            mCurrentPath = file.getPath();
            mView.refreshData(false);
        }
        return isRemoved;
    }

    /**
     * 点击返回显示的文件夹
     *
     * @return 是否返回
     */
    public boolean backFolder() {
        if (mFileStack.size() > 1) {
            mFileStack.pop();
            mView.removeTab();
            File file = mFileStack.peek();
            mCurrentPath = file.getPath();
            mView.refreshData(false);
            return true;
        }
        return false;
    }

    public boolean openFile(Context context, File file) {
        return mFileModel.openFile(context, file);
    }

    private boolean isFileExists(String path) {
        return mFileModel.isFileExists(path);
    }

    private boolean isFolderExists(String path) {
        return mFileModel.isFolderExists(path);
    }

    private boolean addFile(String filePath) {
        return mFileModel.addFile(filePath);
    }

    private boolean addFolder(String folderPath) {
        return mFileModel.addFolder(folderPath);
    }

    private boolean deleteFile(String filePath) {
        return mFileModel.deleteFile(filePath);
    }

    private boolean deleteFolder(String folderPath) {
        return mFileModel.deleteFolder(folderPath);
    }

    private boolean cutFile(String srcFilePath, String destFilePath) {
        return mFileModel.moveFile(srcFilePath, destFilePath);
    }

    private boolean cutFolder(String srcFolderPath, String destFolderPath) {
        return mFileModel.moveFolder(srcFolderPath, destFolderPath);
    }

    private boolean copyFile(String srcFilePath, String destFilePath) {
        return mFileModel.copyFile(srcFilePath, destFilePath);
    }

    private boolean copyFolder(String srcFolderPath, String destFolderPath) {
        return mFileModel.copyFolder(srcFolderPath, destFolderPath);
    }

}