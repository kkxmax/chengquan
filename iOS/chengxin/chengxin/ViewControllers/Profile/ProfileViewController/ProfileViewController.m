//
//  ProfileViewController.m
//  chengxin
//
//  Created by seniorcoder on 11/3/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "ProfileViewController.h"

@interface ProfileViewController ()
{
    AVPlayerViewController *avPlayerVC;
}
@end

@implementation ProfileViewController
@synthesize profileImageView, videoPlayerView;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    // Set Profile Image
    [profileImageView setImage:self.selectedImage];
}

#pragma mark - IBAction
- (IBAction)onBackAction:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

- (IBAction)onPlayAction:(id)sender {
    NSString *urlStr = [[NSBundle mainBundle] pathForResource:@"video.mp4" ofType:nil];
    NSURL *url = [NSURL fileURLWithPath:urlStr];
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
}
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
