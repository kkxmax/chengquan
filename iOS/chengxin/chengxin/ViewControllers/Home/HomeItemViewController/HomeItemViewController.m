//
//  HomeItemViewController.m
//  chengxin
//
//  Created by seniorcoder on 10/27/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "HomeItemViewController.h"
#import "HomeItemTableViewCell.h"
//#import "HomeItemDetailViewController.h"
#import "Global.h"

@interface HomeItemViewController ()

@end

@implementation HomeItemViewController
@synthesize slideItemScrollView, slideItemPageCtrl, homeItemTableView;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    [self.homeItemTableView registerNib:[UINib nibWithNibName:@"HomeItemTableViewCell" bundle:nil] forCellReuseIdentifier:@"HomeItemCellIdentifier"];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    self.navigationController.navigationBar.hidden = YES;
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    // Set slide page time
    [self showSlideShow];
    slideItemPageTimer = [NSTimer scheduledTimerWithTimeInterval:SLIDE_SECOND target:self selector:@selector(PageMove) userInfo:nil repeats:YES];
}

#pragma mark - Page Slide
- (void)showSlideShow {
    slideItemScrollView.contentSize = CGSizeMake(slideItemScrollView.frame.size.width * NUMBER_OF_SLIDE_IMAGES, slideItemScrollView.frame.size.height);
    CGRect ViewSize = slideItemScrollView.bounds;
    for(int i = 0; i <= NUMBER_OF_SLIDE_IMAGES; i++)
    {
        UIImageView *imageView = [[UIImageView alloc] initWithFrame:ViewSize];
        imageView.contentMode = UIViewContentModeScaleToFill;
        [imageView setImage:[UIImage imageNamed:[NSString stringWithFormat:@"%d.png", (FAMILIAR_IMAGE_START + i)]]];
        [slideItemScrollView addSubview:imageView];
        ViewSize = CGRectOffset(ViewSize, slideItemScrollView.bounds.size.width, 0);
    }
}
- (void)PageMove {
    CGFloat pageSize = slideItemScrollView.frame.size.width;
    NSInteger nCurrentPage = 0;
    // if this is the last page return
    if(slideItemScrollView.contentOffset.x >= slideItemScrollView.frame.size.width * NUMBER_OF_SLIDE_IMAGES) {
        [slideItemScrollView setContentOffset:CGPointMake(0.0, slideItemScrollView.contentOffset.y) animated:YES];
    } else {
        [slideItemScrollView setContentOffset:CGPointMake(slideItemScrollView.contentOffset.x + pageSize, slideItemScrollView.contentOffset.y) animated:YES];
        nCurrentPage = (NSInteger)(slideItemScrollView.contentOffset.x / slideItemScrollView.frame.size.width) + 1;
    }
    [slideItemPageCtrl setCurrentPage:nCurrentPage];
}

#pragma mark - TableView
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    HomeItemTableViewCell *homeItemTableCell = (HomeItemTableViewCell *)[tableView dequeueReusableCellWithIdentifier:@"HomeItemCellIdentifier" forIndexPath:indexPath];
    return homeItemTableCell;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return 5;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    CGFloat homeTableCellHeight = 155.f;
    return homeTableCellHeight;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
   // [[NSNotificationCenter defaultCenter] postNotificationName:SHOW_HOMEITEMDETAIL_VIEW_NOTIFICATION object:indexPath];
    
}

#pragma mark - Action
- (IBAction)addAction:(id)sender {
}
@end
