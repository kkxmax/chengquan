//
//  ProfileViewController.m
//  chengxin
//
//  Created by seniorcoder on 11/3/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "ProfileViewController.h"
#import "Global.h"
#import "UIImageView+WebCache.h"
#import "Reachability.h"
#import "GeneralUtil.h"

@interface ProfileViewController ()
{
    AVPlayerViewController *avPlayerVC;
    AVPlayer *player;
    UIAlertView *netStatusAlert;
}
@property (nonatomic) Reachability *internetReachability;

@end

@implementation ProfileViewController
@synthesize profileImageView, videoPlayerView;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(reachabilityChanged:) name:kReachabilityChangedNotification object:nil];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    // Set Profile Image
    if(self.selectedDic) {
        if(![self.selectedDic[@"imgUrl"] isEqualToString:@""])
            [profileImageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, self.selectedDic[@"imgUrl"]]]];
        if(![self.selectedDic[@"videoTitle"] isEqualToString:@""])
            self.videoTitleLabel.text = self.selectedDic[@"videoTitle"];
        if(![self.selectedDic[@"videoComment"] isEqualToString:@""])
            self.videoCommentTextView.text = self.selectedDic[@"videoComment"];
    }
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    
    self.internetReachability = [Reachability reachabilityForInternetConnection];
    [self.internetReachability startNotifier];
    [self updateInterfaceWithReachability:self.internetReachability];
}

- (void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
    if (player) {
        [player pause];
    }
    if (avPlayerVC) {
        [avPlayerVC.view removeFromSuperview];
        avPlayerVC = nil;
    }
}
/*!
 * Called by Reachability whenever status changes.
 */
- (void) reachabilityChanged:(NSNotification *)note
{
    Reachability* curReach = [note object];
    NSParameterAssert([curReach isKindOfClass:[Reachability class]]);
    [self updateInterfaceWithReachability:curReach];
}

- (void)updateInterfaceWithReachability:(Reachability *)reachability
{
    if (reachability == self.internetReachability)
    {
        NetworkStatus networkStatus = [reachability currentReachabilityStatus];
        switch (networkStatus)
        {
            case NotReachable:
            {
                netStatusAlert = [[UIAlertView alloc] initWithTitle:@"" message:@"没有网路连接。" delegate:nil cancelButtonTitle:nil otherButtonTitles:@"确定", nil];
                [netStatusAlert show];
                break;
            }
                
            case ReachableViaWWAN:
            {
                break;
            }
                
            case ReachableViaWiFi:
            {
                [self onPlayAction:nil];
                break;
            }
        }
    }
    
}

#pragma mark - IBAction
- (IBAction)onBackAction:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

- (IBAction)onPlayAction:(id)sender {
    if (sender != nil) {
        netStatusAlert = [[UIAlertView alloc] initWithTitle:@"" message:@"当前属于3G／4G网路环境，您确认要播放？" delegate:self cancelButtonTitle:@"取消" otherButtonTitles:@"确定", nil];
        [netStatusAlert show];
    } else {
        if ([GeneralUtil isInternetConnection]) {
            NSURL *url = [NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, self.selectedDic[@"videoUrl"]]];
            if(avPlayerVC)
            {
                [avPlayerVC.view removeFromSuperview];
                avPlayerVC = nil;
            }
#if XCODE_8_VERSION
            AVPlayer *player = [[AVPlayer alloc] initWithURL:url];
            // create a player view controller
            avPlayerVC = [[AVPlayerViewController alloc]init];
            avPlayerVC.view.frame = videoPlayerView.frame;
            [self addChildViewController:avPlayerVC];
            [self.view addSubview:avPlayerVC.view];
            avPlayerVC.player = player;
            [player play];
#else
            if (@available(iOS 11.0, *)) {
                AVPlayer *player = [[AVPlayer alloc] initWithURL:url];
                // create a player view controller
                avPlayerVC = [[AVPlayerViewController alloc]init];
                avPlayerVC.view.frame = videoPlayerView.frame;
                [self addChildViewController:avPlayerVC];
                [self.view addSubview:avPlayerVC.view];
                avPlayerVC.player = player;
                [player play];
            } else {
                AVPlayer *player = [[AVPlayer alloc] initWithURL:url];
                // create a player view controller
                avPlayerVC = [[AVPlayerViewController alloc]init];
                avPlayerVC.view.frame = videoPlayerView.frame;
                [self.view addSubview:avPlayerVC.view];
                avPlayerVC.player = player;
                [player play];
            }
#endif
        } else {
            netStatusAlert = [[UIAlertView alloc] initWithTitle:@"" message:@"没有网路连接。" delegate:nil cancelButtonTitle:nil otherButtonTitles:@"确定", nil];
            [netStatusAlert show];
        }
        
    }
    
    
}

-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
    if(buttonIndex == 1) {
        if ([GeneralUtil isInternetConnection]) {
            NSURL *url = [NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, self.selectedDic[@"videoUrl"]]];
            if(avPlayerVC)
            {
                [avPlayerVC.view removeFromSuperview];
                avPlayerVC = nil;
            }
            
            AVPlayer *player = [[AVPlayer alloc] initWithURL:url];
            // create a player view controller
            avPlayerVC = [[AVPlayerViewController alloc]init];
            [self.view addSubview:avPlayerVC.view];
            avPlayerVC.view.frame = videoPlayerView.frame;
            avPlayerVC.player = player;
            [player play];
        } else {
            netStatusAlert = [[UIAlertView alloc] initWithTitle:@"" message:@"没有网路连接。" delegate:nil cancelButtonTitle:nil otherButtonTitles:@"确定", nil];
            [netStatusAlert show];
        }
        
    }
}
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/
- (void)dealloc
{
    [[NSNotificationCenter defaultCenter] removeObserver:self name:kReachabilityChangedNotification object:nil];
}
@end
