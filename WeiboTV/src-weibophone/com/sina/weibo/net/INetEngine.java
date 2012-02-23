package com.sina.weibo.net;

import java.util.List;

import android.content.Context;

import com.sina.weibo.exception.WeiboApiException;
import com.sina.weibo.exception.WeiboIOException;
import com.sina.weibo.exception.WeiboParseException;
import com.sina.weibo.models.AdList;
import com.sina.weibo.models.BlackList;
import com.sina.weibo.models.CommentList;
import com.sina.weibo.models.CommentMessageList;
import com.sina.weibo.models.FanList;
import com.sina.weibo.models.FavHotWordList;
import com.sina.weibo.models.GroupList;
import com.sina.weibo.models.HotWordList;
import com.sina.weibo.models.LocationList;
import com.sina.weibo.models.MBlogCRNumList;
import com.sina.weibo.models.MBlogList;
import com.sina.weibo.models.MessageList;
import com.sina.weibo.models.MessageSendResult;
import com.sina.weibo.models.NearByBlogList;
import com.sina.weibo.models.NearByUserInfoList;
import com.sina.weibo.models.NetResult;
import com.sina.weibo.models.SkinList;
import com.sina.weibo.models.SpeedLog;
import com.sina.weibo.models.SquareItemList;
import com.sina.weibo.models.UnreadNum;
import com.sina.weibo.models.UrlItemList;
import com.sina.weibo.models.User;
import com.sina.weibo.models.UserInfo;
import com.sina.weibo.models.UserInfoList;
import com.sina.weibo.models.VersionInfo;

public interface INetEngine {
    public User login(String username, String password)
            throws WeiboParseException, WeiboIOException, WeiboApiException;

    public User login(User user) throws WeiboParseException, WeiboIOException,
            WeiboApiException;

    // public String[] getAuthCode() throws NetException, WeiboIOException;

    public User regist(String username, String password, String question,
            String answer, String cpt, String code, String nick, String gender,
            String uuid) throws WeiboIOException, WeiboParseException,
            WeiboApiException;

    /**
     * Get someone's information.
     * 
     * @param u
     *            User 当前用户
     * @param uid
     *            someone's id, can be null
     * @param nick
     *            someone's nickname, can be null
     * @return UserInfo
     * @throws WeiboIOException
     * @throws WeiboApiException
     */
    public UserInfo getUserInfo(User u, String uid, String nick)
            throws WeiboParseException, WeiboIOException, WeiboApiException;

    /**
     * Search users
     * 
     * @param u
     *            user.
     * @param keyword
     *            what you want to search.
     * @param page
     *            the index of current page in pages, 1 is the first page.
     * @param pagesize
     *            the number of items in the page, default is 10.
     * @return UserInfoList
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public UserInfoList searchUser(User u, String keyword, int page,
            int pagesize) throws WeiboIOException, WeiboParseException,
            WeiboApiException;

    /**
     * get at suggestion users
     * 
     * @param u
     *            user.
     * @param keyword
     *            what you want to search.
     * @return UserInfoList
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public UserInfoList getAtSuggestion(User u, String keyword)
            throws WeiboIOException, WeiboParseException, WeiboApiException;

    /**
     * 获取推荐用户 Top users
     * 
     * @param uid
     *            the public user id.
     * @param pagesize
     *            the number of items in the page, default is 10.
     * @return UserInfoList.
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public UserInfoList getTopUserList(String uid, int pagesize)
            throws WeiboIOException, WeiboParseException, WeiboApiException;

    /**
     * get users you may like
     * 
     * @param User
     * @return UserInfoList
     * @throws WeiboIOException
     * @throws WeiboParseException
     * @throws WeiboApiException
     */
    public UserInfoList getGuessUserList(User u) throws WeiboIOException,
            WeiboParseException, WeiboApiException;

    /**
     * 获取 名人榜，人气草根榜
     * 
     * @param User
     *            u
     * @param cat
     *            1为名人榜,2为人气草根榜
     * @param page
     * @param pageSize
     * @return
     * @throws WeiboIOException
     * @throws WeiboParseException
     * @throws WeiboApiException
     */
    public UserInfoList getUserRankList(User u, int cat, int page, int pageSize)
            throws WeiboIOException, WeiboParseException, WeiboApiException;

    /**
     * Edit the user information.
     * 
     * @param u
     *            : User
     * @param gender
     *            : boy or girl? @see com.sina.weibo.Constants
     * @param rename
     *            : whether user auto modify by server if duplicate
     * @param nick
     *            : user nick name
     * @param intro
     *            : a introduce for a special suer
     * @return NetResult: success or not
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public NetResult editUserInfo(User u, int gender, String nick,
            String intro, String portraitFile, int rename)
            throws WeiboIOException, WeiboParseException, WeiboApiException;

    /**
     * 取得黑名单列表
     * 
     * @param u
     * @param page
     * @param pageSize
     * @return
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public BlackList fetchBlackList(User u, int page, int pageSize)
            throws WeiboIOException, WeiboParseException, WeiboApiException;

    /**
     * 将 uid 表示的用户从黑名单中删除
     * 
     * @param u
     * @param uid
     * @return
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     * @throws HttpException
     */
    public NetResult delFromBlackList(User u, String uid)
            throws WeiboIOException, WeiboParseException, WeiboApiException;

    /**
     * 将 uid 表示的用户添加到黑名单中
     * 
     * @param u
     * @param uid
     * @return
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     * @throws HttpException
     */
    public NetResult addToBlackList(User u, String uid)
            throws WeiboIOException, WeiboParseException, WeiboApiException;

    /**
     * Post a new mblog with or without a picture.
     * 
     * @param u
     *            user.
     * @param content
     *            the content of the mblog.
     * @param picpath
     *            the path of picture. If it is a null, then no picture will be
     *            uploaded.
     * @param latitude
     *            经度
     * @param longitude
     *            纬度
     * @param isLocated
     *            是否有定位信息
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public NetResult postNewMBlog(User u, String content, double latitude,
            double longitude, String picpath, String poiid, String poititle,
            String xid, boolean offset, boolean isLocated)
            throws WeiboIOException, WeiboParseException, WeiboApiException;

    /**
     * Forward a mblog.
     * 
     * @param u
     *            user.
     * @param mblogId
     *            the id of mblog.
     * @param mblogUid
     *            the uid of the mblog author.
     * @param reason
     *            why forward it? (can be null)
     * @param content
     *            the content
     * @return NetResult
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public NetResult forwardMBlog(User u, String mblogId, String mblogUid,
            String reason, String content, boolean isComment)
            throws WeiboIOException, WeiboParseException, WeiboApiException;

    /**
     * Delete a Mblog.
     * 
     * @param u
     *            user.
     * @param id
     *            the id of mblog.
     * @param act
     *            do delete
     * @return NetResult success or not
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public NetResult deleteMBlog(User u, String mblogid)
            throws WeiboIOException, WeiboParseException, WeiboApiException;

    /**
     * Get home blog list.
     * 
     * @param u
     *            current user.
     * @param gid
     *            group id.
     * @param picsize
     *            the size of picture which the user prefer.
     * @param maxid
     *            the max mblog id in the list you will get
     * @param pageSize
     *            the number of mblogs in the page, default is 10.
     * @return MBlogList.
     * 
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public MBlogList getHomeBlogList(User u, String gid, int picsize,
            String maxId, int pageSize) throws WeiboIOException,
            WeiboParseException, WeiboApiException;

    /**
     * Get someone's blog list.
     * 
     * @param u
     *            user.
     * @param uid
     *            someone's id
     * @param picsize
     *            the size of picture which the user prefer.
     * @param page
     *            the index of current page in pages, 1 is the first page.
     * @param pagesize
     *            the number of mblogs in the page, default is 10.
     * @return MBlogList.
     * 
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public MBlogList getUserMBlogList(User u, String uid, int picsize,
            int page, int pageSize) throws WeiboIOException,
            WeiboParseException, WeiboApiException;

    /**
     * Get a mblog.
     * 
     * @param u
     *            user.
     * @param mid
     *            the blog's id
     * @param picsize
     *            the size of picture which the user prefer.
     * @return MBlogList this List contains only one element.
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public MBlogList getSingleMBlog(User u, String mid, int picsize)
            throws WeiboIOException, WeiboParseException, WeiboApiException;

    /**
     * Search mblogs 根据关键词搜索微博
     * 
     * @param u
     *            user.
     * @param keyword
     *            what you want to search.
     * @param picsize
     *            the size of picture which the user prefer.
     * @param page
     *            the index of current page in pages, 1 is the first page.
     * @param pagesize
     *            the number of mblogs in the page, default is 10.
     * @return MBlogList
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public MBlogList searchMBlog(User u, String keyword, int picsize, int page,
            int pagesize) throws WeiboIOException, WeiboParseException,
            WeiboApiException;

    /**
     * Get @who blog list.
     * 
     * @param u
     *            user.
     * @param uid
     *            user uid. null means it's me.
     * @param picsize
     *            the size of picture which the user prefer.
     * @param page
     *            the index of current page in pages, 1 is the first page.
     * @param pagesize
     *            the number of mblogs in the page, default is 10.
     * @return MBlogList
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     * 
     */
    public MBlogList getAtMsgList(User u, String uid, int picsize, int page,
            int pagesize) throws WeiboIOException, WeiboParseException,
            WeiboApiException;

    /**
     * Get "look around" / "hot foward" list.
     * 
     * @param uid
     *            public uid.
     * @param cat
     *            1 for "look around", 2 for "hot forward"
     * @param picsize
     *            the size of picture which the user prefer.
     * @param page
     *            the index of current page in pages, 1 is the first page.
     * @param pagesize
     *            the number of mblogs in the page, default is 10.
     * @return a two element array. array[0] is the total count of \@me mblogs,
     *         and array[1] is ArrayList of mblogs.
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public MBlogList getNewsList(String uid, String cat, int picsize, int page,
            int pagesize) throws WeiboIOException, WeiboParseException,
            WeiboApiException;

    /**
     * Get my favorite mblogs.
     * 
     * @param u
     *            user.
     * @param picsize
     *            the size of picture which the user prefer.
     * @param page
     *            the index of current page in pages, 1 is the first page.
     * @param pagesize
     *            the number of mblogs in the page, default is 10.
     * @return MBlogList
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public MBlogList getFavMBlogList(User u, int picsize, int page, int pagesize)
            throws WeiboIOException, WeiboParseException, WeiboApiException;

    /**
     * Add a favorite mblog.
     * 
     * @param u
     *            user.
     * @param id
     *            the id of the mblog
     * @return NetResult success or not
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public NetResult addFavMBlog(User u, String id) throws WeiboIOException,
            WeiboParseException, WeiboApiException;

    /**
     * Delete a favorite mblog.
     * 
     * @param u
     *            user.
     * @param mid
     *            the id of the mblog
     * @return NetResult success or not
     * 
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public NetResult deleteFavMBlog(User u, String id, String mid)
            throws WeiboIOException, WeiboParseException, WeiboApiException;

    /**
     * Get comments list about a mblog.
     * 
     * @param u
     *            user.
     * @param srcuid
     *            mblog author's id.
     * @param srcid
     *            the id of mblog.
     * @param page
     *            the index of current page in pages, 1 is the first page.
     * @param pagesize
     *            the number of comments in the page, default is 10.
     * @return CommentList.
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public CommentList getAttCommentList(User u, String srcuid, String srcid,
            int page, int pagesize) throws WeiboIOException,
            WeiboParseException, WeiboApiException;

    /**
     * Add a comment for a mblog. In this case, cmtuid and cmtid in the comment
     * is useless.
     * 
     * @param u
     *            user.
     * @param srcuid
     *            mblog author's uid
     * @param srcid
     *            the id of the mblog
     * @param content
     *            comment
     * @param rt
     *            post a new MBlog or not
     * @return NetResult success or not
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public NetResult addComment(User u, String srcuid, String srcid,
            String content, boolean rt) throws WeiboIOException,
            WeiboParseException, WeiboApiException;

    /**
     * Reply a comment for a comment. In this case, cmtuid and cmtid in the
     * comment must be set.
     * 
     * @param u
     *            user.
     * @param srcuid
     *            mblog author's uid
     * @param srcid
     *            the id of the mblog
     * @param cmtuid
     *            comment author's uid
     * @param cmtid
     *            the id of the comment
     * @param content
     *            comment
     * @param rt
     *            post a new MBlog or not
     * @return NetResult success or not
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public NetResult replyComment(User u, String srcuid, String srcid,
            String cmtuid, String cmtid, String content, boolean rt)
            throws WeiboIOException, WeiboParseException, WeiboApiException;

    /**
     * Delete a comment. In this case, cmtuid and cmtid in the comment must be
     * set.
     * 
     * @param u
     *            user.
     * @param srcuid
     *            mblog author's uid
     * @param srcid
     *            the id of the mblog
     * @param cmtuid
     *            comment author's uid
     * @param cmtid
     *            the id of the comment
     * @return NetResult success or not
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public NetResult deleteComment(User u, String srcuid, String srcid,
            String cmtuid, String cmtid) throws WeiboIOException,
            WeiboParseException, WeiboApiException;

    /**
     * Get my comment blog list.
     * 
     * @param u
     *            user.
     * @param boxtype
     *            1 is inbox, 2 is outbox
     * @param page
     *            the index of current page in pages, 1 is the first page.
     * @param pageSize
     *            the number of comment message in the page, default is 10.
     * @return CommentMessageList.
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public CommentMessageList getMyCommentList(User u, int boxtype, int page,
            int pageSize) throws WeiboIOException, WeiboParseException,
            WeiboApiException;

    /**
     * Send a private message.
     * 
     * @param u
     *            user.
     * @param nick
     *            someone's nick
     * @param content
     *            the content of the message.
     * @return NetResult success or not
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public NetResult sendMessage(User u, String nick, String content)
            throws WeiboIOException, WeiboParseException, WeiboApiException;

    /**
     * Send a private message with an attachment
     * 
     * @param u
     *            user.
     * @param uid
     *            recipient's uid
     * @param nick
     *            recipient's nick
     * @param content
     *            the content of the message.
     * @param fid
     *            attachment file id (sender's fid,recipient's fid in
     *            VDisk),format => afid,bfid
     * @return NetResult success or not
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public MessageSendResult sendMessage(User u, String uid, String nick,
            String content, String fid) throws WeiboIOException,
            WeiboParseException, WeiboApiException;

    /**
     * Get private messages list. If the uid is null, this function will get all
     * messages come from all other users, each user has only one record to tell
     * you how many messages who send to the user. If the uid is not null, it
     * will get messages only come from who you specified.
     * 
     * @param u
     *            user.
     * @param uid
     *            the user's uid.
     * @param page
     *            the index of current page in pages, 1 is the first page.
     * @param pageSize
     *            the number of message in the page, default is 10.
     * @return MessageList.
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public MessageList getMessageList(User u, String uid, int page, int pageSize)
            throws WeiboIOException, WeiboParseException, WeiboApiException;

    /**
     * Get private messages list. If the uid is null, this function will get all
     * messages come from all other users, each user has only one record to tell
     * you how many messages who send to the user. If the uid is not null, it
     * will get messages only come from who you specified.
     * 
     * @param u
     *            user.
     * @param uid
     *            the user's uid.
     * @param since_id
     *            the msgid, get messages whose msgid is bigger than since_id
     * @param max_id
     *            the msgid, get messages whose msgid is smaller than max_id
     * @param count
     *            the number of message , default is 20.
     * @return MessageList.
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public MessageList getMessageList(User u, String uid, String since_id,
            String max_id, int count) throws WeiboIOException,
            WeiboParseException, WeiboApiException;

    /**
     * 删除某条私信
     * 
     * @param u
     *            当前账户信息
     * @param uid
     *            对方的UID
     * @param msgId
     *            私信ID
     * @return NetResult 删除是否成功
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public NetResult deleteOneMessage(User u, String uid, String msgId)
            throws WeiboIOException, WeiboParseException, WeiboApiException;

    /**
     * Deleta all private messages come from or sent to someone.
     * 
     * @param u
     *            user.
     * @param uid
     *            someone's uid
     * @return NetResult success or not
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public NetResult deleteMessages(User u, String uid)
            throws WeiboIOException, WeiboParseException, WeiboApiException;

    /**
     * Get attention/fans list.
     * 
     * @param u
     *            user.
     * @param cat
     *            list type, 1 for fans' list, 0 for attention list.
     * @param uid
     *            someone's id
     * @param sort
     *            sort method, 1 for sorting by attention time, 2 for sorting by
     *            capital, 3 for sorting by update time. Only available when cat
     *            is 1.
     * @param keyword
     *            nickname filter, can be null.
     * @param lastmblog
     *            whether contains the last mblog. 0 for no, 1 for yes.
     * @param page
     *            the index of current page in pages, 1 is the first page.
     * @param pageSize
     *            the number of mblogs in the page, default is 10.
     * @return a two element array. array[0] is the total count of mblogs, and
     *         array[1] is ArrayList of attentions or fans.
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public FanList getAttentionList(User u, int cat, String uid, int sort,
            String keyword, int lastmblog, int page, int pageSize)
            throws WeiboIOException, WeiboParseException, WeiboApiException;

    /**
     * Add an attention.
     * 
     * @param u
     *            user.
     * @param uid
     *            someone's uid
     * @return NetResult success or not
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public NetResult addAttention(User u, String uid) throws WeiboIOException,
            WeiboParseException, WeiboApiException;

    /**
     * Cancel an attention.
     * 
     * @param u
     *            user.
     * @param uid
     *            someone's uid
     * @return NetResult success or not
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public NetResult cancelAttention(User u, String uid)
            throws WeiboIOException, WeiboParseException, WeiboApiException;

    /**
     * Get favorite hot word list.
     * 
     * @param u
     *            user.
     * @param uid
     *            someone's uid, null for current user.
     * @param page
     *            the index of current page in pages, 1 is the first page.
     * @param pageSize
     *            the number of words in the page, default is 10.
     * @return FavHotWordList
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public FavHotWordList getFavHotWordList(User u, String uid, int page,
            int pageSize) throws WeiboIOException, WeiboParseException,
            WeiboApiException;

    /**
     * Add/Delete a Mblog.
     * 
     * @param u
     *            user.
     * @param id
     *            the id of mblog.
     * @param act
     *            act=0->add act=1->remove
     * @return NetResult success or not
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public NetResult dealTopic(User u, int act, String fav)
            throws WeiboIOException, WeiboParseException, WeiboApiException;

    /**
     * get billboard of the hot word
     * 
     * @param u
     *            current user
     * @param type
     *            0=> recent 1 hour 2=> today 3=> this week
     * @param page
     *            the index of current page in pages, 1 is the first page.
     * @param pageSize
     *            the number of words in the page, default is 10.
     * @return
     * @throws WeiboIOException
     * @throws WeiboParseException
     * @throws WeiboApiException
     */
    public HotWordList getHotWordList(User u, int type, int page, int pageSize)
            throws WeiboIOException, WeiboParseException, WeiboApiException;

    /**
     * Get unread numbers.
     * 
     * @param u
     *            user
     * @return UnreadNum.
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public UnreadNum getUnreadNum(User u) throws WeiboIOException,
            WeiboParseException, WeiboApiException;

    /**
     * 举报
     * 
     * @see NetEngine#report(User, String, String, String, int, int)
     * @param usr
     * @param srcuid
     * @param mblogId
     * @param reason_
     * @return NetResult
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public NetResult report(User usr, String srcuid, String mblogId,
            String reason_) throws WeiboIOException, WeiboParseException,
            WeiboApiException;

    /**
     * 举报某篇微博
     * 
     * @param usr
     * @param mblogId
     * @param reason_
     * @return NetResult
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public NetResult reportBlog(User usr, String mblogId, String reason_)
            throws WeiboIOException, WeiboParseException, WeiboApiException;

    /**
     * 举报某用户
     * 
     * @param usr
     * @param srcuid
     * @param reason_
     * @return NetResult
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public NetResult reportUser(User usr, String srcuid, String reason_)
            throws WeiboIOException, WeiboParseException, WeiboApiException;

    /**
     * 取得最新的版本信息
     * 
     * @return
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     * @throws HttpException
     */
    public VersionInfo getLatestVersion(boolean force) throws WeiboIOException,
            WeiboParseException, WeiboApiException;

    /**
     * Get group list for current user
     * 
     * @param usr
     *            current
     * @return GroupList
     * 
     * @throws WeiboIOException
     * @throws WeiboParseException
     * @throws WeiboApiException
     */
    public GroupList getGroupList(User usr) throws WeiboIOException,
            WeiboParseException, WeiboApiException;

    /**
     * 获取微博评论和转发数量
     * 
     * @param usr
     *            current user
     * @param mblogId
     *            微博id,支持多个
     * @return
     * @throws WeiboIOException
     * @throws WeiboParseException
     * @throws WeiboApiException
     */
    public MBlogCRNumList getMblogCRNum(User usr, String... mblogId)
            throws WeiboIOException, WeiboParseException, WeiboApiException;

    /**
     * Download and save the picture to the local storage.
     * 
     * @param url
     *            the url of picture
     * @param savedir
     *            the directory to save picture, you can get it by calling
     *            context.getCacheDir().
     * @return the path of picture in the local storage.
     * @throws WeiboIOException
     * @throws WeiboApiException
     */
    public String getPicture(String url, String savedir)
            throws WeiboIOException, WeiboApiException;

    /**
     * Download and save the picture to the local storage.
     * 
     * @param url
     *            the url of picture
     * @param savedir
     *            the directory to save picture, you can get it by calling
     *            context.getCacheDir().
     * @param needReload
     *            if true,reload picture from net
     * @param callback
     *            reporting download state by using this callback object
     * @param name
     *            assign this file name
     * @return the path of picture in the local storage.
     * @throws WeiboIOException
     * @throws WeiboApiException
     */
    public String getPicture(String url, String savedir, boolean needReload,
            IDownloadState callback, String name) throws WeiboIOException,
            WeiboApiException;

    /**
     * 获取广场所需信息
     * 
     * @param usr
     *            用户信息
     * @return
     * @throws WeiboIOException
     * @throws WeiboParseException
     * @throws WeiboApiException
     */
    public SquareItemList getSquare(User usr) throws WeiboIOException,
            WeiboParseException, WeiboApiException;

    /**
     * 获取测速用的Url列表
     * 
     * @param usr
     *            用户信息
     * @return
     * @throws WeiboIOException
     * @throws WeiboParseException
     * @throws WeiboApiException
     */
    public UrlItemList getSpeedUrlItemList(User usr) throws WeiboIOException,
            WeiboParseException, WeiboApiException;

    /**
     * 提交测速结果
     * 
     * @param usr
     * @param log
     * @return NetResult 是否提交成功
     * @throws WeiboIOException
     * @throws WeiboParseException
     * @throws WeiboApiException
     */
    public NetResult postSpeedLog(User usr, SpeedLog log)
            throws WeiboIOException, WeiboParseException, WeiboApiException;

    /**
     * 提交测速结果
     * 
     * @param usr
     * @param logs
     * @return NetResult 是否提交成功
     * @throws WeiboIOException
     * @throws WeiboParseException
     * @throws WeiboApiException
     */
    public NetResult postSpeedLog(User usr, List<SpeedLog> logs)
            throws WeiboIOException, WeiboParseException, WeiboApiException;

    /**
     * 获取广告信息列表
     * 
     * @return
     * @throws WeiboIOException
     * @throws WeiboParseException
     * @throws WeiboApiException
     */
    public AdList getAdList() throws WeiboIOException, WeiboParseException,
            WeiboApiException;

    /**
     * 按照规则生成 UA
     * 
     * @param ctx
     * @return
     */
    // TODO static
    public String generateUA(Context ctx);

    /**
     * 用户数据收集
     * 
     * @return
     */
    // TODO static
    public NetResult uploadUserLog(Context context) throws WeiboApiException,
            WeiboParseException, WeiboIOException;

    public SkinList getSkinList(Context context, User usr)
            throws WeiboApiException, WeiboParseException, WeiboIOException;

    public LocationList getDistanceByRange(Context context, User usr,
            double lat, double lon, String query, int num, int start)
            throws WeiboApiException, WeiboParseException, WeiboIOException;

    public NearByUserInfoList getNearByPeople(User usr, String lat, String lon,
            int page, int pageSize) throws WeiboIOException,
            WeiboParseException, WeiboApiException;

    public NearByBlogList getNearByBlog(User usr, String lat, String lon,
            int page, int pageSize) throws WeiboIOException,
            WeiboParseException, WeiboApiException;

    /**
     * 确保当前User对象中包含有效的oauth_token
     * 
     * @param context
     * @param oauth_token
     */
    // TODO static
    public boolean prepareVaildUser(Context context, String oauth_token);

    public NetResult postNavigaterBlog() throws WeiboIOException,
            WeiboParseException, WeiboApiException;
}
