<header class="header">
    <div class="header-left">
        <i class="fa-solid fa-bars"></i>
        <a href="index.html" class="header-logo">
            <img src="./img/logo-fold.png" alt="" />
        </a>
    </div>
    <div class="header-right">
        <!-- Chuông thông báo Admin -->
        <div id="admin-notification"
             style="position:relative;display:inline-block;margin-right:24px;cursor:pointer;">
            <i class="fa fa-bell"
               style="font-size: 23px; color: #222; transition: color 0.2s;"></i>
            <span id="notif-badge"
                  style="display:none;position:absolute;top:-5px;right:-8px;background:#ff2e2e;
                         color:#fff;font-size:13px;border-radius:10px;padding:2px 7px;
                         z-index:1001;font-weight:bold;min-width:24px;text-align:center;">
            0</span>
            <!-- Popup danh sách thông báo -->
            <div id="notif-list"
                 style="display:none;position:absolute;top:32px;right:0;background:#fff;
                        border:1px solid #eee;width:330px;max-height:370px;overflow:auto;
                        box-shadow:0 4px 18px #0002;z-index:1002;border-radius:8px;">
                <div style="font-weight:bold;padding:12px 14px 6px 14px;border-bottom:1px solid #eee;color:#222;">
                    Thông báo mới
                </div>
                <ul id="notif-ul"
                    style="margin:0;padding:10px 16px;list-style:none;font-size:15px;min-height:60px;"></ul>
            </div>
        </div>

        <div class="account">
            <img src="https://images.unsplash.com/photo-1726554881162-ceeb7d68be8c?q=80&w=1888&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
                 alt=""
                 style="width:38px;height:38px;border-radius:50%;object-fit:cover;border:1.5px solid #eee;"/>
        </div>
    </div>
</header>
